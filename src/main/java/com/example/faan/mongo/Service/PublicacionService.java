package com.example.faan.mongo.Service;

import com.example.faan.mongo.Repository.PublicacionRepository;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.modelos.TipoPublicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private CounterService counterService;


    public Publicacion crearPublicacion(Publicacion publicacion1) {
        BigInteger nuevaPublicacionId = counterService.getNextSequence("publicacion_id");

        // Asignar automáticamente la fecha de publicación actual
        publicacion1.setFecha_publicacion(LocalDateTime.now());

        // Creamos la nueva publicación manteniendo la fecha como String
        Publicacion publicacion = Publicacion.builder()
                .id(nuevaPublicacionId)
                .nombre(publicacion1.getNombre())
                .raza(publicacion1.getRaza())
                .sexo(publicacion1.getSexo())
                .tipoAnimal(publicacion1.getTipoAnimal())
                .descripcionEspecifica(publicacion1.getDescripcionEspecifica())
                .tipoPublicacion(publicacion1.getTipoPublicacion())
                .fecha(publicacion1.getFecha())
                .fecha_publicacion(publicacion1.getFecha_publicacion())
                .ubicacion(publicacion1.getUbicacion())
                .estadoRescatado(publicacion1.getEstadoRescatado())
                .foto(publicacion1.getFoto())
                .build();
        return publicacionRepository.save(publicacion);
    }

    public List<Publicacion> obtenerTodasLasPublicaciones() {
        return publicacionRepository.findAll();
    }
    public List<Publicacion> obtenerPublicacionesPorTipo(TipoPublicacion tipo) {
        return publicacionRepository.findByTipoPublicacion(tipo);
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

    public List<Publicacion> obtenerPublicacionesPorEstado(boolean estadoRescatado) {
        return publicacionRepository.findByEstadoRescatado(estadoRescatado);
    }
    public List<Publicacion> publicacionesConEstadoFalse(List<Publicacion> publicaciones) {
        List<Publicacion> publicacionesFiltradas = new ArrayList<>();
        for (Publicacion publicacion : publicaciones) {
            if (Boolean.FALSE.equals(publicacion.getEstadoRescatado())) {
                publicacionesFiltradas.add(publicacion);
            }
        }
        return publicacionesFiltradas;
    }
    public List<Publicacion> publicacionesConEstadoTrue(List<Publicacion> publicaciones) {
        List<Publicacion> publicacionesFiltradas = new ArrayList<>();
        for (Publicacion publicacion : publicaciones) {
            if (Boolean.TRUE.equals(publicacion.getEstadoRescatado())) {
                publicacionesFiltradas.add(publicacion);
            }
        }
        return publicacionesFiltradas;
    }


    public List<Publicacion> listarPublicacionesRescatadas() {
        List<Publicacion> todasLasPublicaciones = publicacionRepository.findAll();
        List<Publicacion> publicacionesRescatadas = publicacionesConEstadoTrue(todasLasPublicaciones);
        return publicacionesRescatadas.stream()
                .filter(publicacion -> publicacion.getTipoPublicacion() == TipoPublicacion.ENCONTRADO || publicacion.getTipoPublicacion() == TipoPublicacion.PERDIDO)
                .collect(Collectors.toList());
    }
}