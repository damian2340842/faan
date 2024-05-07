package com.example.faan.mongo.Repository;

import com.example.faan.mongo.modelos.EnumsFijo.Role;
import com.example.faan.mongo.modelos.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByUsername(String username);

    // Puedes agregar métodos de consulta adicionales según tus necesidades

    Usuario findByEmailAndUsername(String email, String username);
    Usuario findByVerificationToken(String verificationToken);

    boolean existsByRole(Role role);

    public Usuario findByEmail(String email);

    public Usuario findByTokenPassword(String tokenPassword);



}
