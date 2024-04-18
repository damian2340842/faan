package com.example.faan.mongo.Service;

import com.example.faan.mongo.Repository.PublicacionRepository;
import com.example.faan.mongo.modelos.Publicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;


    @Transactional
    public Publicacion crearPublicacion(Publicacion publicacion) {
        return publicacionRepository.save(publicacion);
    }

    public List<Publicacion> obtenerTodasLasPublicaciones() {
        return publicacionRepository.findAll();
    }

    public Optional<Publicacion> obtenerPublicacionPorId(Long id) {
        return publicacionRepository.findById(id);
    }

    @Transactional
    public Publicacion actualizarPublicacion(Long id, Publicacion nuevaPublicacion) {
        Optional<Publicacion> publicacionExistente = publicacionRepository.findById(id);

        if (publicacionExistente.isPresent()) {
            Publicacion publicacion = publicacionExistente.get();
            publicacion.setNombre(nuevaPublicacion.getNombre());
            publicacion.setRaza(nuevaPublicacion.getRaza());
            publicacion.setSexo(nuevaPublicacion.getSexo());
            publicacion.setDescripcionEspecifica(nuevaPublicacion.getDescripcionEspecifica());
            publicacion.setUbicacion(nuevaPublicacion.getUbicacion());
            publicacion.setFoto(nuevaPublicacion.getFoto());
            publicacion.setFecha(nuevaPublicacion.getFecha());
            // Puedes agregar aquí más campos para actualizar si tienes más campos en el modelo de publicación.
            return publicacionRepository.save(publicacion);
        } else {
            throw new IllegalArgumentException("Publicación con ID " + id + " no encontrada.");
        }
    }

    @Transactional
    public void eliminarPublicacion(Long id) {
        publicacionRepository.deleteById(id);
    }


}