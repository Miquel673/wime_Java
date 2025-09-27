package com.wime.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/rutinas")
public class Rutina {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/eliminar")
    public ResponseEntity<Map<String, Object>> eliminarRutina(
            @RequestParam("id") Integer idRutina,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        Integer idUsuario = (Integer) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            response.put("success", false);
            response.put("message", "❌ Sesión no iniciada");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (idRutina == null) {
            response.put("success", false);
            response.put("message", "⚠️ Falta ID de la rutina");
            return ResponseEntity.badRequest().body(response);
        }

        String deleteSql = "DELETE FROM rutinas WHERE IDRutina = ? AND IDusuarios = ?";
        int rowsAffected = jdbcTemplate.update(deleteSql, idRutina, idUsuario);

        if (rowsAffected > 0) {
            response.put("success", true);
            response.put("message", "✅ Rutina eliminada");

            // Agregar notificación
            String mensaje = "Se ha eliminado una Rutina.";
            String insertNotif = "INSERT INTO notificaciones (IDusuarios, tipo, mensaje) VALUES (?, 'rutina', ?)";
            jdbcTemplate.update(insertNotif, idUsuario, mensaje);

        } else {
            response.put("success", false);
            response.put("message", "❌ No se pudo eliminar");
        }

        return ResponseEntity.ok(response);
    }
}
