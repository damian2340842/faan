package com.example.faan.mongo.Repository;

import com.example.faan.mongo.modelos.Publicacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicacionRepository extends MongoRepository<Publicacion, Long> {

    List<Publicacion> findByNombre(String nombre);

    Optional<Publicacion> findById(Long id);

    List<Publicacion> findByDescripcionEspecifica(String descripcionEspecifica);


}