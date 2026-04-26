package com.example.MediaStreaming.repository;

import com.example.MediaStreaming.model.Usuario;
import com.example.MediaStreaming.model.Visualizacion;
import com.example.MediaStreaming.model.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisualizacionRepository extends JpaRepository<Visualizacion, Integer> {

    List<Visualizacion> findByUsuarioOrderByIdDesc(Usuario usuario);

    List<Visualizacion> findByUsuarioAndEstadoOrderByIdDesc(Usuario usuario, String estado);

    Optional<Visualizacion> findByUsuarioAndContenido(Usuario usuario, Contenido contenido);

    void deleteByContenido(Contenido contenido);

    void deleteByUsuario(Usuario usuario);
}
