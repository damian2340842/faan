package com.example.faan.mongo.Service;

import com.example.faan.mongo.Repository.PublicacionRepository;
import com.example.faan.mongo.modelos.Publicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private CounterService counterService;


    @Transactional
    public Publicacion crearPublicacion(Publicacion publicacion1) {
        BigInteger nuevaPublicacionId = counterService.getNextSequence("publicacion_id");
        Publicacion publicacion = Publicacion.builder()
                .id(nuevaPublicacionId)
                .nombre(publicacion1.getNombre())
                .raza(publicacion1.getRaza())
                .sexo(publicacion1.getSexo())
                .tipoAnimal(publicacion1.getTipoAnimal())
                .descripcionEspecifica(publicacion1.getDescripcionEspecifica())
                .tipoPublicacion(publicacion1.getTipoPublicacion())
                .fecha(publicacion1.getFecha())
                .ubicacion(publicacion1.getUbicacion())
                .estadoRescatado(publicacion1.getEstadoRescatado())
                .foto(publicacion1.getFoto())
                .build();
        return publicacionRepository.save(publicacion);
    }



    public List<Publicacion> obtenerTodasLasPublicaciones() {
        return publicacionRepository.findAll();
    }

    public Optional<Publicacion> obtenerPublicacionPorId(BigInteger id) {
        return publicacionRepository.findById(id);
    }

    @Transactional
    public Publicacion actualizarPublicacion(BigInteger id, Publicacion nuevaPublicacion) {
        Optional<Publicacion> publicacionExistente = publicacionRepository.findById(id);

        if (publicacionExistente.isPresent()) {
            Publicacion publicacion = publicacionExistente.get();
            publicacion.setNombre(nuevaPublicacion.getNombre());
            publicacion.setRaza(nuevaPublicacion.getRaza());
            publicacion.setSexo(nuevaPublicacion.getSexo());
            publicacion.setDescripcionEspecifica(nuevaPublicacion.getDescripcionEspecifica());
            publicacion.setFecha(nuevaPublicacion.getFecha());
            publicacion.setUbicacion(nuevaPublicacion.getUbicacion());
            publicacion.setEstadoRescatado(nuevaPublicacion.getEstadoRescatado());
            publicacion.setFoto(nuevaPublicacion.getFoto());
            // Puedes agregar aquí más campos para actualizar si tienes más campos en el modelo de publicación.
            return publicacionRepository.save(publicacion);
        } else {
            throw new IllegalArgumentException("Publicación con ID " + id + " no encontrada.");
        }
    }
    @Transactional
    public void eliminarPublicacion(BigInteger id) {
        publicacionRepository.deleteById(id);
    }



}