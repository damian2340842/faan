package com.example.faan.mongo.Repository.secundary;

import com.example.faan.mongo.modelos.secundary.Notificacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NotificacionRepository extends MongoRepository<Notificacion, String> {

    List<Notificacion> findByEstadoMensaje(String estado);

    @Query("{'estadoMensaje': 'A', 'eliminarPublicacion': {$gte: ?0, $lte: ?1}}")
    List<Notificacion> findNotificacionEliminarPublicacion(Date fechaInicio, Date fechaFin);
}
