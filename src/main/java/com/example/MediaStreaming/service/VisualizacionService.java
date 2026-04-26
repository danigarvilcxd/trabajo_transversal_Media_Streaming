package com.example.MediaStreaming.service;

import com.example.MediaStreaming.model.Contenido;
import com.example.MediaStreaming.model.Usuario;
import com.example.MediaStreaming.model.Visualizacion;
import com.example.MediaStreaming.repository.VisualizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisualizacionService {

    @Autowired
    private VisualizacionRepository visualizacionRepository;

    public List<Visualizacion> obtenerHistorial(Usuario usuario) {
        return visualizacionRepository.findByUsuarioOrderByIdDesc(usuario);
    }

    public List<Visualizacion> obtenerContinuarViendo(Usuario usuario) {
        return visualizacionRepository.findByUsuarioAndEstadoOrderByIdDesc(usuario, "viendo");
    }

    public void registrarOActualizarVisualizacion(Usuario usuario, Contenido contenido, String estado, Integer tiempoVisto) {
        Visualizacion visualizacion = visualizacionRepository
                .findByUsuarioAndContenido(usuario, contenido)
                .orElseGet(Visualizacion::new);

        visualizacion.setUsuario(usuario);
        visualizacion.setContenido(contenido);
        visualizacion.setEstado(estado);
        visualizacion.setTiempoVisto(tiempoVisto);

        visualizacionRepository.save(visualizacion);
    }
}
