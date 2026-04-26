package com.example.MediaStreaming.Controller;

import com.example.MediaStreaming.model.Usuario;
import com.example.MediaStreaming.model.TipoUsuario;
import com.example.MediaStreaming.repository.FavoritoRepository;
import com.example.MediaStreaming.repository.GeneroRepository;
import com.example.MediaStreaming.repository.UsuarioRepository;
import com.example.MediaStreaming.repository.VisualizacionRepository;
import com.example.MediaStreaming.service.ContenidoService;
import com.example.MediaStreaming.service.GeneroService;
import com.example.MediaStreaming.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ContenidoService contenidoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private VisualizacionRepository visualizacionRepository;

    @Autowired
    private FavoritoRepository favoritoRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuariosTotal", usuarioRepository.count());
        model.addAttribute("contenidosTotal", contenidoService.contarTodos());
        model.addAttribute("visualizacionesTotal", visualizacionRepository.count());
        model.addAttribute("favoritosTotal", favoritoRepository.count());
        return "admin/dashboard";
    }
    
    @GetMapping("/usuarios")
    public String gestionarUsuarios(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("tiposUsuario", TipoUsuario.values());
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(
            HttpSession session,
            RedirectAttributes redirectAttributes,
            @RequestParam(required = false) Integer id,
            @RequestParam String apodo,
            @RequestParam String correo,
            @RequestParam(required = false) String contrasena,
            @RequestParam String tipoUsuario) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }

        try {
            usuarioService.guardar(id, apodo, correo, contrasena, TipoUsuario.fromValor(tipoUsuario));
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/eliminar")
    public String eliminarUsuario(HttpSession session, RedirectAttributes redirectAttributes, @RequestParam Integer id) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }

        if (usuario.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "No puedes eliminar tu propio usuario mientras tienes la sesion activa.");
            return "redirect:/admin/usuarios";
        }

        usuarioService.eliminar(id);
        return "redirect:/admin/usuarios";
    }
    
    @GetMapping("/contenido")
    public String gestionarContenido(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("contenidos", contenidoService.obtenerTodos());
        model.addAttribute("generos", generoRepository.findAll());
        return "admin/contenido";
    }

    @PostMapping("/contenido/guardar")
    public String guardarContenido(
            HttpSession session,
            RedirectAttributes redirectAttributes,
            @RequestParam(required = false) Integer id,
            @RequestParam String titulo,
            @RequestParam(required = false) String descripcion,
            @RequestParam String tipo,
            @RequestParam(required = false) String urlArchivo,
            @RequestParam(required = false) MultipartFile imagenArchivo,
            @RequestParam(required = false) Integer duracion,
            @RequestParam(required = false) List<Integer> generoIds) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }

        try {
            contenidoService.guardar(id, titulo, descripcion, tipo, urlArchivo, imagenArchivo, duracion, generoIds);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/contenido";
    }

    @PostMapping("/contenido/eliminar")
    public String eliminarContenido(HttpSession session, @RequestParam Integer id) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }

        contenidoService.eliminar(id);
        return "redirect:/admin/contenido";
    }
    
    @GetMapping("/generos")
    public String gestionarGeneros(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("generos", generoService.obtenerTodos());
        return "admin/generos";
    }

    @PostMapping("/generos/guardar")
    public String guardarGenero(
            HttpSession session,
            RedirectAttributes redirectAttributes,
            @RequestParam(required = false) Integer id,
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }

        try {
            generoService.guardar(id, nombre, descripcion);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/generos";
    }

    @PostMapping("/generos/eliminar")
    public String eliminarGenero(HttpSession session, @RequestParam Integer id) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }

        generoService.eliminar(id);
        return "redirect:/admin/generos";
    }
    
    @GetMapping("/reportes")
    public String verReportes(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.ADMIN) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuariosTotal", usuarioRepository.count());
        model.addAttribute("contenidosTotal", contenidoService.contarTodos());
        model.addAttribute("generosTotal", generoRepository.count());
        model.addAttribute("visualizacionesTotal", visualizacionRepository.count());
        model.addAttribute("favoritosTotal", favoritoRepository.count());
        return "admin/reportes";
    }
}
