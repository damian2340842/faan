package com.example.faan.mongo.publicidad.repository;

import com.example.faan.mongo.publicidad.model.Publicidad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Repository
public interface PublicidadRepository extends MongoRepository<Publicidad, BigInteger > {


    List<Publicidad> findByTitulo(String titulo);

    Optional<Publicidad> findById(BigInteger id);

    void deleteById(BigInteger id);

    Publicidad save(Publicidad publicidad);
    boolean existsByTituloAndDescripcion(String titulo, String descripcion);

}
