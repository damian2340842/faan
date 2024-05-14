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

    /**
     * Method to check if a user exists by email
     *
     * @param email email to check.
     * @return true if the user exists, false otherwise.
     */
    Boolean existsByEmailIgnoreCase(String email);

    /**
     * Method to check if a user exists by username
     *
     * @param username username to check.
     * @return true if the user exists, false otherwise.
     */
    Boolean existsByUsernameIgnoreCase(String username);

    /**
     * Method to check if a user exists by phone
     *
     * @param phone phone to check.
     * @return true if the user exists, false otherwise.
     */
    Boolean existsByTelefono(String phone);

}
