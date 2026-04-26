package com.example.MediaStreaming.service;

import com.example.MediaStreaming.model.TipoUsuario;
import com.example.MediaStreaming.model.Usuario;
import com.example.MediaStreaming.repository.FavoritoRepository;
import com.example.MediaStreaming.repository.UsuarioRepository;
import com.example.MediaStreaming.repository.VisualizacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private VisualizacionRepository visualizacionRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    
    /**
     * Registra un nuevo usuario
     * @param apodo - Apodo del usuario
     * @param correo - Correo del usuario
     * @param contrasena - Contraseña del usuario
     * @return Usuario creado o null si ya existe
     */
    public Usuario registrar(String apodo, String correo, String contrasena) {
        // Validar que el correo no exista
        if (usuarioRepository.existsByCorreo(correo)) {
            return null; // El correo ya está registrado
        }
        
        // Validar que el apodo no exista
        if (usuarioRepository.existsByApodo(apodo)) {
            return null; // El apodo ya está registrado
        }
        
        // Crear y guardar el nuevo usuario
        Usuario nuevoUsuario = new Usuario(correo, contrasena, apodo);
        return usuarioRepository.save(nuevoUsuario);
    }
    
    /**
     * Valida las credenciales de un usuario
     * @param correo - Correo del usuario
     * @param contrasena - Contraseña del usuario
     * @return Usuario si las credenciales son válidas, null si no
     */
    public Usuario autenticar(String correo, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        
        // Verificar si el usuario existe y la contraseña es correcta
        if (usuario.isPresent() && usuario.get().getContrasena().equals(contrasena)) {
            return usuario.get();
        }
        
        return null; // Credenciales inválidas
    }
    
    /**
     * Obtiene un usuario por su ID
     * @param id - ID del usuario
     * @return Usuario si existe, null si no
     */
    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    
    /**
     * Obtiene un usuario por su correo
     * @param correo - Correo del usuario
     * @return Usuario si existe, null si no
     */
    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }
    
    /**
     * Obtiene un usuario por su apodo
     * @param apodo - Apodo del usuario
     * @return Usuario si existe, null si no
     */
    public Usuario obtenerPorApodo(String apodo) {
        return usuarioRepository.findByApodo(apodo).orElse(null);
    }

    @Transactional
    public Usuario guardar(Integer id, String apodo, String correo, String contrasena, TipoUsuario tipoUsuario) {
        Usuario usuario = id == null
                ? new Usuario()
                : usuarioRepository.findById(id).orElse(new Usuario());

        Optional<Usuario> usuarioConCorreo = usuarioRepository.findByCorreo(correo);
        if (usuarioConCorreo.isPresent() && !usuarioConCorreo.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo.");
        }

        Optional<Usuario> usuarioConApodo = usuarioRepository.findByApodo(apodo);
        if (usuarioConApodo.isPresent() && !usuarioConApodo.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese apodo.");
        }

        usuario.setApodo(apodo);
        usuario.setCorreo(correo);
        if (contrasena != null && !contrasena.isBlank()) {
            usuario.setContrasena(contrasena);
        }
        usuario.setTipo_usuario(tipoUsuario);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(Integer id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return;
        }

        favoritoRepository.deleteByUsuario(usuario);
        visualizacionRepository.deleteByUsuario(usuario);
        usuarioRepository.delete(usuario);
    }
}
