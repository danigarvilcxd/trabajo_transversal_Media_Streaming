package com.example.MediaStreaming.Controller;

import com.example.MediaStreaming.service.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    
    @Autowired
    private ContenidoService contenidoService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("recomendados", contenidoService.obtenerRecomendados());
        model.addAttribute("totalContenidos", contenidoService.contarTodos());
        return "index";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
        }
        model.addAttribute("recomendados", contenidoService.obtenerRecomendados());
        return "dashboard";
    }
}
