package com.example.faan.mongo.Repository.secundary;

import com.example.faan.mongo.modelos.secundary.Mi_Ubicacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Mi_UbicacionRepository extends MongoRepository<Mi_Ubicacion, String> {
    List<Mi_Ubicacion> findByUserId(String userId);
}