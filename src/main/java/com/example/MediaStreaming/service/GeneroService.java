package com.example.MediaStreaming.service;

import com.example.MediaStreaming.model.Contenido;
import com.example.MediaStreaming.model.Genero;
import com.example.MediaStreaming.repository.ContenidoRepository;
import com.example.MediaStreaming.repository.GeneroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private ContenidoRepository contenidoRepository;

    public List<Genero> obtenerTodos() {
        return generoRepository.findAll();
    }

    @Transactional
    public Genero guardar(Integer id, String nombre, String descripcion) {
        Genero genero = id == null
                ? new Genero()
                : generoRepository.findById(id).orElse(new Genero());

        Optional<Genero> generoConNombre = generoRepository.findByNombre(nombre);
        if (generoConNombre.isPresent() && !generoConNombre.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe un genero con ese nombre.");
        }

        genero.setNombre(nombre);
        genero.setDescripcion(descripcion);

        return generoRepository.save(genero);
    }

    @Transactional
    public void eliminar(Integer id) {
        Genero genero = generoRepository.findById(id).orElse(null);
        if (genero == null) {
            return;
        }

        List<Contenido> contenidos = contenidoRepository.findByGenerosId(id);
        for (Contenido contenido : contenidos) {
            contenido.getGeneros().remove(genero);
        }

        generoRepository.delete(genero);
    }
}
