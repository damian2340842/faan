package com.example.faan.mongo.Service.mongoService;

import com.example.faan.mongo.Repository.secundary.NotificacionRepository;
import com.example.faan.mongo.modelos.secundary.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Override
    public Notificacion save(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    @Override
    public Notificacion update(String id, Notificacion notificacion) {
        Notificacion notificacionFound = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));
        notificacionFound.setEstadoMensaje(notificacion.getEstadoMensaje());
        return notificacionRepository.save(notificacionFound);
    }



    @Override
    public Notificacion findById(String id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));
    }
}
