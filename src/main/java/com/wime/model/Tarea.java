package com.wime.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException; // ✅ IMPORT CORRECTO para Spring Boot 3.x
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@SpringBootApplication
@RestController
public class Tarea {

    public static void main(String[] args) {
        SpringApplication.run(Tarea.class, args);
    }

    @PostMapping("/tareas/crear")
    public Map<String, Object> crearTarea(
            @RequestParam Map<String, String> datos,
            HttpSession session
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        // Verificar sesión
        Integer idUsuario = (Integer) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            respuesta.put("success", false);
            respuesta.put("message", "❌ Sesión no iniciada");
            return respuesta;
        }

        // Obtener y validar campos
        String titulo = datos.getOrDefault("titulo", "").trim();
        String prioridad = datos.getOrDefault("prioridad", "").trim();
        String fechaLimite = datos.getOrDefault("fecha_limite", "").trim();
        String descripcion = datos.getOrDefault("descripcion", "").trim();
        String estado = "pendiente";

        if (titulo.isEmpty() || prioridad.isEmpty() || fechaLimite.isEmpty()) {
            respuesta.put("success", false);
            respuesta.put("message", "⚠️ Faltan campos obligatorios");
            return respuesta;
        }

        // Conexión a la base de datos
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Wime", "root", "")) {

            // Insertar la tarea
            String sqlTarea = "INSERT INTO tareas (id_usuario, titulo, prioridad, fecha_limite, descripcion, estado) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlTarea)) {
                stmt.setInt(1, idUsuario);
                stmt.setString(2, titulo);
                stmt.setString(3, prioridad);
                stmt.setString(4, fechaLimite);
                stmt.setString(5, descripcion);
                stmt.setString(6, estado);
                stmt.executeUpdate();
            }

            // Insertar notificación
            String mensaje = "Se ha creado una nueva tarea: " + titulo;
            String sqlNotif = "INSERT INTO notificaciones (id_usuario, tipo, mensaje) VALUES (?, 'tarea', ?)";
            try (PreparedStatement stmtNotif = conn.prepareStatement(sqlNotif)) {
                stmtNotif.setInt(1, idUsuario);
                stmtNotif.setString(2, mensaje);
                stmtNotif.executeUpdate();
            }

            respuesta.put("success", true);
            respuesta.put("message", "✅ Tarea creada con éxito");

        } catch (SQLException e) {
            e.printStackTrace();
            respuesta.put("success", false);
            respuesta.put("message", "❌ Error al guardar");
        }

        return respuesta;
    }
}
