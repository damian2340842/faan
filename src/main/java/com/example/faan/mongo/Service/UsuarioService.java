package com.example.faan.mongo.Service;

import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.exception.ObjectNotFoundException;
import com.example.faan.mongo.modelos.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario findByUsername(String username) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByUsername(username);
        return optionalUsuario.orElse(null);
    }


    public Usuario findByPersonaEmail(String identificacion) {
        return usuarioRepository.findByEmail(identificacion);
    }

    public Usuario findByTokenPassword(String tokenPassword) {
        return usuarioRepository.findByTokenPassword(tokenPassword);
    }

    @Transactional
    public Boolean updatePassword(String userId, String password) {
        Usuario usuario = usuarioRepository.findById(userId).
                orElseThrow(() -> new ObjectNotFoundException("User with id " + userId + " not found"));
        if (usuario != null) {
            usuario.setPassword(bCryptPasswordEncoder.encode(password));
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    public void actualizarUsuario(String id, Usuario usuario) {
        Optional<Usuario> usuarioExistenteOptional = usuarioRepository.findById(id);
        if (usuarioExistenteOptional.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOptional.get();
            usuarioExistente.setNombre(usuario.getNombre());
            usuarioExistente.setApellido(usuario.getApellido());
            usuarioExistente.setUsername(usuario.getUsername());
            usuarioExistente.setPassword(usuario.getPassword());
            usuarioExistente.setEmail(usuario.getEmail());
            usuarioExistente.setDireccion(usuario.getDireccion());
            usuarioExistente.setTelefono(usuario.getTelefono());
            usuarioRepository.save(usuarioExistente);
        } else {
            throw new RuntimeException("Usuario no encontrado con el ID: " + id);
        }
    }

    public void eliminarUsuario(String id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> obtenerUsuarioPorId(String id) {
        return usuarioRepository.findById(id);
    }


    /**
     * Allowed method to validate unique field.
     * @param email email to check.
     */
    public Boolean existsByEmailIgnoreCase(String email) {
        return usuarioRepository.existsByEmailIgnoreCase(email);
    }

    /**
     * Allowed method to validate unique field.
     * @param username username to check.
     */
    public Boolean existsByUsernameIgnoreCase(String username) {
        return usuarioRepository.existsByUsernameIgnoreCase(username);
    }

    /**
     * Allowed method to validate unique field.
     * @param phone phone to check.
     */
    public Boolean existsByTelefono(String phone) {
        return usuarioRepository.existsByTelefono(phone);
    }

}
