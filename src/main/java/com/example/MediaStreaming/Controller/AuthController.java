package com.example.MediaStreaming.Controller;

import com.example.MediaStreaming.model.TipoUsuario;
import com.example.MediaStreaming.model.Usuario;
import com.example.MediaStreaming.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"/login", "/auth/login"})
    public String showLogin() {
        return "login";
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam String correo, @RequestParam String contrasena, 
                       HttpSession session, Model model) {
        // Autenticar el usuario
        Usuario usuario = usuarioService.autenticar(correo, contrasena);
        
        if (usuario != null) {
            // Usuario autenticado, guardar en sesión
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            
            // Redirigir según tipo de usuario
            if (usuario.getTipo_usuario() == TipoUsuario.ADMIN) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/usuario/dashboard";
            }
        } else {
            // Credenciales inválidas
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "login";
        }
    }

    @PostMapping("/auth/register")
    public String register(@RequestParam String apodo, @RequestParam String correo, 
                          @RequestParam String contrasena, @RequestParam String confirmar_contraseña,
                          Model model) {
        
        // Validar que las contraseñas coincidan
        if (!contrasena.equals(confirmar_contraseña)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "login";
        }
        
        // Validar longitud mínima de contraseña
        if (contrasena.length() < 6) {
            model.addAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            return "login";
        }
        
        // Registrar el usuario
        Usuario nuevoUsuario = usuarioService.registrar(apodo, correo, contrasena);
        
        if (nuevoUsuario != null) {
            model.addAttribute("success", "Cuenta creada exitosamente. Inicia sesión.");
            return "login";
        } else {
            model.addAttribute("error", "El correo o apodo ya están registrados");
            return "login";
        }
    }
    
    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

