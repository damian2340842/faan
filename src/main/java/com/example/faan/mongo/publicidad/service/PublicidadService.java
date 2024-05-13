package com.example.faan.mongo.publicidad.service;


import com.example.faan.mongo.Service.CounterService;
import com.example.faan.mongo.file.model.dto.PhotoResponse;
import com.example.faan.mongo.file.service.IPhotoService;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.publicidad.model.Publicidad;
import com.example.faan.mongo.publicidad.repository.PublicidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PublicidadService {

    @Autowired
    private PublicidadRepository publicidadRepository;

    @Autowired
    private CounterService counterService;

    private final IPhotoService photoService;


    public List<Publicidad> obtenerTodasLasPublicidades() {
        return publicidadRepository.findAll();
    }

    public Optional<Publicidad> obtenerPublicidadPorId(BigInteger id) {
        return publicidadRepository.findById(id);
    }

    @Transactional
    public void eliminarPublicidad(BigInteger id) {
        publicidadRepository.deleteById(id);
    }


    @Transactional
    public Publicidad guardarPublicidad(Publicidad publicidad, MultipartFile photo) throws IOException {
        boolean existePublicidad = publicidadRepository.existsByTituloAndDescripcion(publicidad.getTitulo(), publicidad.getDescripcion());

        if (existePublicidad) {
            throw new IllegalArgumentException("Ya existe una publicidad con el mismo título y descripción. Cambie un dato");
        }

        BigInteger nuevoId = counterService.getNextSequence("publicidad_id");
        PhotoResponse photoResponse = photoService.uploadPhoto(photo);

        Publicidad nuevaPublicidad = Publicidad.builder()
                .id(nuevoId)
                .titulo(publicidad.getTitulo())
                .descripcion(publicidad.getDescripcion())
                .photoPublicidad(photoResponse.getMessage())
                .build();

        return publicidadRepository.save(nuevaPublicidad);
    }

    @Transactional
    public Publicidad editarPublicidad(BigInteger id, Publicidad publicidad, MultipartFile photo) throws IOException {
        Optional<Publicidad> publicidadExistente = publicidadRepository.findById(id);

        if (publicidadExistente.isPresent()) {
            Publicidad publicidadAEditar = publicidadExistente.get();

            if (publicidad.getTitulo() != null) {
                publicidadAEditar.setTitulo(publicidad.getTitulo());
            }
            if (publicidad.getDescripcion() != null) {
                publicidadAEditar.setDescripcion(publicidad.getDescripcion());
            }

            if (photo != null) {
                PhotoResponse photoResponse = photoService.uploadPhoto(photo);
                publicidadAEditar.setPhotoPublicidad(photoResponse.getMessage());
            }

            return publicidadRepository.save(publicidadAEditar);
        } else {
            throw new IllegalArgumentException("Publicidad con ID " + id + " no encontrada.");
        }
    }

}