package com.wime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wime.service.UsuarioService;

@Controller
public class LoginController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login"; // Nombre del archivo HTML en 'src/main/resources/templates'
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String nombreUsuario, @RequestParam String contrasena, Model model) {
        if (usuarioService.validarLogin(nombreUsuario, contrasena)) {
            // Redirige a una página de éxito
            return "redirect:/dashboard";
        } else {
            // Muestra un mensaje de error
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
            return "login";
        }
    }
}