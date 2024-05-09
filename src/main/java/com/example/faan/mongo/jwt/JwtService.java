package com.example.faan.mongo.jwt;

import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.exception.ObjectNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    public String SECRET_KEY;

    private final UsuarioRepository usuarioRepository;

    public String getToken(UserDetails empleado) {
        return getToken(generateExtraClaims(empleado), empleado);
    }

    public String getToken(Map<String, Object> extraClaims, UserDetails empleado) {

        JwtBuilder jwtBuilder = Jwts.builder()

                .setSubject(empleado.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));

        for (Map.Entry<String, Object> entry : extraClaims.entrySet()) {
            jwtBuilder.claim(entry.getKey(), entry.getValue());
        }

        return jwtBuilder
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserNameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Claims gelAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = gelAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Map<String, Object> generateExtraClaims(UserDetails user) {
        Map<String, Object> extraClaims = new HashMap<>();
        String idUser = String.valueOf(usuarioRepository.findByUsername(user.getUsername()).orElseThrow(() -> new ObjectNotFoundException("User not found")).getId());

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        extraClaims.put("role", roles);
        extraClaims.put("userId", idUser);

        return extraClaims;
    }
}
