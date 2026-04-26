package com.example.MediaStreaming.repository;

import com.example.MediaStreaming.model.Contenido;
import com.example.MediaStreaming.model.Favorito;
import com.example.MediaStreaming.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {

    List<Favorito> findByUsuarioOrderByIdDesc(Usuario usuario);

    Optional<Favorito> findByUsuarioAndContenido(Usuario usuario, Contenido contenido);

    boolean existsByUsuarioAndContenido(Usuario usuario, Contenido contenido);

    void deleteByUsuarioAndContenido(Usuario usuario, Contenido contenido);

    void deleteByContenido(Contenido contenido);

    void deleteByUsuario(Usuario usuario);
}
