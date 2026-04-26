package com.example.MediaStreaming.service;

import com.example.MediaStreaming.model.Contenido;
import com.example.MediaStreaming.model.Favorito;
import com.example.MediaStreaming.model.Usuario;
import com.example.MediaStreaming.repository.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    public List<Favorito> obtenerFavoritos(Usuario usuario) {
        return favoritoRepository.findByUsuarioOrderByIdDesc(usuario);
    }

    public Set<Integer> obtenerIdsFavoritos(Usuario usuario) {
        List<Favorito> favoritos = obtenerFavoritos(usuario);
        Set<Integer> ids = new HashSet<>();
        for (Favorito favorito : favoritos) {
            if (favorito.getContenido() != null && favorito.getContenido().getId() != null) {
                ids.add(favorito.getContenido().getId());
            }
        }
        return ids;
    }

    public boolean esFavorito(Usuario usuario, Contenido contenido) {
        return favoritoRepository.existsByUsuarioAndContenido(usuario, contenido);
    }

    public void toggleFavorito(Usuario usuario, Contenido contenido) {
        favoritoRepository.findByUsuarioAndContenido(usuario, contenido)
                .ifPresentOrElse(favoritoRepository::delete, () -> {
                    Favorito favorito = new Favorito();
                    favorito.setUsuario(usuario);
                    favorito.setContenido(contenido);
                    favoritoRepository.save(favorito);
                });
    }
}
