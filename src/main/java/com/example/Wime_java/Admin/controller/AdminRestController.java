package com.example.Wime_java.Admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Wime_java.Admin.service.AdminService;
import com.example.Wime_java.model.Usuario;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @Autowired
    private AdminService adminService;

    // 🔹 Listar usuarios
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(adminService.listarUsuarios());
    }

    // 🔹 Cambiar estado
    @PutMapping("/estado/{id}")
    public ResponseEntity<String> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String estado) {

        adminService.cambiarEstado(id, estado);
        return ResponseEntity.ok("Estado actualizado correctamente");
    }

    // 🔹 Cambiar tipo
    @PutMapping("/tipo/{id}")
    public ResponseEntity<String> cambiarTipo(
            @PathVariable Integer id,
            @RequestParam String tipo) {

        adminService.cambiarTipo(id, tipo);
        return ResponseEntity.ok("Tipo actualizado correctamente");
    }

    // 🔹 Eliminar usuario
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer id) {

        adminService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}
