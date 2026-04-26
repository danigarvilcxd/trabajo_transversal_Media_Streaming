package com.example.MediaStreaming.repository;

import com.example.MediaStreaming.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // Buscar usuario por correo
    Optional<Usuario> findByCorreo(String correo);
    
    // Buscar usuario por apodo
    Optional<Usuario> findByApodo(String apodo);
    
    // Validar si el correo existe
    boolean existsByCorreo(String correo);
    
    // Validar si el apodo existe
    boolean existsByApodo(String apodo);
}
