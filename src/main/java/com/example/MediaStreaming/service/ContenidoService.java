package com.example.MediaStreaming.service;

import com.example.MediaStreaming.model.Contenido;
import com.example.MediaStreaming.model.Genero;
import com.example.MediaStreaming.repository.ContenidoRepository;
import com.example.MediaStreaming.repository.FavoritoRepository;
import com.example.MediaStreaming.repository.GeneroRepository;
import com.example.MediaStreaming.repository.VisualizacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ContenidoService {

    @Value("${media.upload-dir}")
    private String uploadDir;

    @Autowired
    private ContenidoRepository contenidoRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private VisualizacionRepository visualizacionRepository;

    public List<Contenido> obtenerTodos() {
        return contenidoRepository.findAll();
    }

    public List<Contenido> buscarPorTitulo(String termino) {
        return contenidoRepository.findByTituloContainingIgnoreCase(termino);
    }

    public List<Contenido> filtrarPorGenero(String genero) {
        return contenidoRepository.findByGenerosNombre(genero);
    }

    public List<Contenido> buscarPorGeneroYTitulo(String genero, String termino) {
        return contenidoRepository.findByGenerosNombreAndTituloContainingIgnoreCase(genero, termino);
    }

    public List<Contenido> obtenerRecomendados() {
        List<Contenido> todos = contenidoRepository.findAll();
        return todos.size() > 4 ? todos.subList(0, 4) : todos;
    }

    public long contarTodos() {
        return contenidoRepository.count();
    }

    public Contenido obtenerPorId(Integer id) {
        return contenidoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Contenido guardar(Integer id, String titulo, String descripcion, String tipo, String urlArchivo,
            MultipartFile imagenArchivo, Integer duracion, List<Integer> generoIds) {
        Contenido contenido = id == null
                ? new Contenido()
                : contenidoRepository.findById(id).orElse(new Contenido());

        contenido.setTitulo(titulo);
        contenido.setDescripcion(descripcion);
        contenido.setTipo(tipo);
        contenido.setUrlArchivo(urlArchivo);
        String imagen = guardarImagen(imagenArchivo);
        if (imagen != null) {
            contenido.setImagen(imagen);
        }
        contenido.setDuracion(duracion);

        Set<Genero> generos = new HashSet<>();
        if (generoIds != null && !generoIds.isEmpty()) {
            generos.addAll(generoRepository.findAllById(generoIds));
        }
        contenido.setGeneros(generos);

        return contenidoRepository.save(contenido);
    }

    private String guardarImagen(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

        String nombreOriginal = archivo.getOriginalFilename();
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf(".")).toLowerCase();
        }

        if (!extension.matches("\\.(jpg|jpeg|png|webp|gif)")) {
            throw new IllegalArgumentException("Solo se permiten imagenes JPG, PNG, WEBP o GIF.");
        }

        String nombreArchivo = UUID.randomUUID() + extension;
        Path carpetaDestino = Paths.get("src", "main", "resources", "static").resolve(uploadDir).normalize();

        try {
            Files.createDirectories(carpetaDestino);
            archivo.transferTo(carpetaDestino.resolve(nombreArchivo));
            return nombreArchivo;
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo guardar la imagen subida.", ex);
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        Contenido contenido = contenidoRepository.findById(id).orElse(null);
        if (contenido == null) {
            return;
        }

        favoritoRepository.deleteByContenido(contenido);
        visualizacionRepository.deleteByContenido(contenido);
        contenido.getGeneros().clear();
        contenidoRepository.delete(contenido);
    }

    public List<Contenido> buscar(String genero, String busqueda) {
        if (genero != null && !genero.isBlank() && !genero.equals("Todos")) {
            if (busqueda != null && !busqueda.isBlank()) {
                return buscarPorGeneroYTitulo(genero, busqueda);
            }
            return filtrarPorGenero(genero);
        }

        if (busqueda != null && !busqueda.isBlank()) {
            return buscarPorTitulo(busqueda);
        }

        return obtenerTodos();
    }
}
