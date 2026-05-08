package com.example.Wime_java.Admin.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.Wime_java.Admin.dto.AdminNotificacionDTO;

import com.example.Wime_java.Admin.service.AdminService;
import com.example.Wime_java.model.Usuario;

import jakarta.servlet.http.HttpSession;


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


        @GetMapping("/notificaciones")
    public ResponseEntity<List<AdminNotificacionDTO>> listarNotificaciones() {
        return ResponseEntity.ok(adminService.listarNotificaciones());
    }

    @DeleteMapping("/notificaciones/{idNotificacion}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long idNotificacion) {
        adminService.eliminarNotificacion(idNotificacion);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/notificaciones")
    public ResponseEntity<Void> eliminarTodasLasNotificaciones() {
        adminService.eliminarTodasLasNotificaciones();
        return ResponseEntity.noContent().build();
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
        try {
            adminService.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reporte/pdf")
    public ResponseEntity<InputStreamResource> exportarReporteAdmin(HttpSession session) {
        Long idAdmin = (Long) session.getAttribute("id_usuario");
        if (idAdmin == null) {
            return ResponseEntity.status(401).build();
        }

        ByteArrayInputStream bis = adminService.generarReporteAdminPdf(idAdmin);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_admin_" + idAdmin + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
