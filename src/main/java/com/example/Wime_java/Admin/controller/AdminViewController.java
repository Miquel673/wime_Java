package com.example.Wime_java.Admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/Admin/HTML/Wime_Interfaz_AdminDashBoard.html";
    }

    @GetMapping("/usuarios-vista")
    public String usuariosVista() {
        return "redirect:/Admin/HTML/Wime_Interfaz_AdminDashBoard.html";
    }
}