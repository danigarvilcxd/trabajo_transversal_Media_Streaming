package com.example.MediaStreaming.repository;

import com.example.MediaStreaming.model.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, Integer> {

    List<Contenido> findByTituloContainingIgnoreCase(String titulo);

    List<Contenido> findByGenerosNombre(String nombre);

    List<Contenido> findByGenerosId(Integer id);

    List<Contenido> findByGenerosNombreAndTituloContainingIgnoreCase(String nombre, String titulo);
}
