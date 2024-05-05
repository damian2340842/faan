package com.example.faan.mongo.Service.mongoService;

import com.example.faan.mongo.modelos.secundary.Notificacion;

public interface NotificacionService {
    public Notificacion save(Notificacion notificacion);

    public Notificacion update(String id, Notificacion notificacion);

    public Notificacion findById(String id);
}

