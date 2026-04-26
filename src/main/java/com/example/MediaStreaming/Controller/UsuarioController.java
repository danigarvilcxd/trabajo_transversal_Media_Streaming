package com.example.MediaStreaming.Controller;

import com.example.MediaStreaming.model.Contenido;
import com.example.MediaStreaming.model.Genero;
import com.example.MediaStreaming.model.Usuario;
import com.example.MediaStreaming.model.TipoUsuario;
import com.example.MediaStreaming.model.Visualizacion;
import com.example.MediaStreaming.service.ContenidoService;
import com.example.MediaStreaming.service.FavoritoService;
import com.example.MediaStreaming.service.GeneroService;
import com.example.MediaStreaming.service.VisualizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private ContenidoService contenidoService;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private VisualizacionService visualizacionService;

    @Autowired
    private FavoritoService favoritoService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.USUARIO) {
            return "redirect:/login";
        }

        List<Visualizacion> continuarViendo = visualizacionService.obtenerContinuarViendo(usuario);
        List<Contenido> recomendados = contenidoService.obtenerRecomendados();

        model.addAttribute("usuario", usuario);
        model.addAttribute("continuarViendo", continuarViendo);
        model.addAttribute("recomendados", recomendados);

        return "usuario/dashboard";
    }

    @GetMapping("/contenido")
    public String verContenido(HttpSession session,
                               @RequestParam(required = false) String genero,
                               @RequestParam(required = false) String busqueda,
                               Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.USUARIO) {
            return "redirect:/login";
        }

        if (genero != null && genero.equals("Todos")) {
            genero = null;
        }

        List<Genero> generos = generoService.obtenerTodos();
        List<Contenido> contenidos = contenidoService.buscar(genero, busqueda);
        Set<Integer> favoritosIds = favoritoService.obtenerIdsFavoritos(usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("generos", generos);
        model.addAttribute("contenidos", contenidos);
        model.addAttribute("selectedGenero", genero == null ? "Todos" : genero);
        model.addAttribute("busqueda", busqueda == null ? "" : busqueda);
        model.addAttribute("favoritosIds", favoritosIds);

        return "usuario/contenido";
    }

    @GetMapping("/contenido/ver/{id}")
    public String marcarVerContenido(@PathVariable Integer id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.USUARIO) {
            return "redirect:/login";
        }

        Contenido contenido = contenidoService.obtenerPorId(id);
        if (contenido == null) {
            return "redirect:/usuario/contenido";
        }

        visualizacionService.registrarOActualizarVisualizacion(usuario, contenido, "viendo", 0);
        return "redirect:/usuario/contenido/detalle/" + contenido.getId();
    }

    @GetMapping("/contenido/detalle/{id}")
    public String verDetalleContenido(@PathVariable Integer id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.USUARIO) {
            return "redirect:/login";
        }

        Contenido contenido = contenidoService.obtenerPorId(id);
        if (contenido == null) {
            return "redirect:/usuario/contenido";
        }

        boolean esFavorito = favoritoService.esFavorito(usuario, contenido);

        model.addAttribute("usuario", usuario);
        model.addAttribute("contenido", contenido);
        model.addAttribute("esFavorito", esFavorito);

        return "usuario/detalle";
    }

    @GetMapping("/contenido/favorito/{id}")
    public String cambiarFavorito(@PathVariable Integer id,
                                  @RequestParam(required = false, defaultValue = "contenido") String redirect,
                                  HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.USUARIO) {
            return "redirect:/login";
        }

        Contenido contenido = contenidoService.obtenerPorId(id);
        if (contenido != null) {
            favoritoService.toggleFavorito(usuario, contenido);
        }

        if ("favoritos".equalsIgnoreCase(redirect)) {
            return "redirect:/usuario/favoritos";
        }

        if ("detalle".equalsIgnoreCase(redirect)) {
            return "redirect:/usuario/contenido/detalle/" + id;
        }

        return "redirect:/usuario/contenido";
    }

    @GetMapping("/historial")
    public String verHistorial(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.USUARIO) {
            return "redirect:/login";
        }

        List<Visualizacion> historial = visualizacionService.obtenerHistorial(usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("historial", historial);

        return "usuario/historial";
    }

    @GetMapping("/favoritos")
    public String verFavoritos(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || usuario.getTipo_usuario() != TipoUsuario.USUARIO) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("favoritos", favoritoService.obtenerFavoritos(usuario));

        return "usuario/favoritos";
    }
}
