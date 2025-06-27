package cibertec.org.Consultorio_Psicologia.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import cibertec.org.Consultorio_Psicologia.entity.Rol;
import cibertec.org.Consultorio_Psicologia.entity.Usuario;
import cibertec.org.Consultorio_Psicologia.service.UsuarioService;

@Controller
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario usuario) {
        usuario.setRol(Rol.PACIENTE);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEstado(true);
        usuarioService.guardar(usuario);
        return "redirect:/login?registrado";
    }

    @GetMapping("/redirect")
    public String redireccionSegunRol(Authentication authentication) {
        if (authentication == null) {
            // Log para depuración
            System.err.println("[REDIRECT] Authentication es null");
            return "redirect:/login?error";
        }
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getRol() == null) {
                System.err.println("[REDIRECT] Usuario sin rol: " + usuario.getEmail());
                return "redirect:/login?error";
            }
            if (usuario.getRol() == Rol.ADMIN) {
                return "redirect:/admin/panel";
            } else if (usuario.getRol() == Rol.PACIENTE) {
                return "redirect:/paciente/dashboard";
            } else {
                System.err.println("[REDIRECT] Rol desconocido: " + usuario.getRol());
                return "redirect:/login?error";
            }
        } else {
            System.err.println("[REDIRECT] Usuario no encontrado: " + email);
            return "redirect:/login?error";
        }
    }
}