package com.example.faan.mongo.Repository;

import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicacionRepository extends MongoRepository<Publicacion, Long> {

    List<Publicacion> findByNombre(String nombre);

    Optional<Publicacion> findById(BigInteger id);

    List<Publicacion> findByDescripcionEspecifica(String descripcionEspecifica);
    void deleteById(BigInteger id);
    List<Publicacion> findByEstadoRescatado(boolean estadoRescatado);

    List<Publicacion> findByTipoPublicacion(TipoPublicacion tipoPublicacion);

    List<Publicacion> findByEstadoFavoritos(boolean estadoFavorito);
    List<Publicacion> findByEstadoRescatadoAndTipoPublicacion(boolean estadoRescatado, TipoPublicacion tipoPublicacion);


    List<Publicacion> findByUsuario(Usuario usuario);
}