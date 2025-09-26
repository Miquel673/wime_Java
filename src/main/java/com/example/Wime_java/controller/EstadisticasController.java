package com.example.Wime_java.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Wime_java.model.Usuario;

import jakarta.servlet.http.HttpSession;

@RestController
public class EstadisticasController {

    private final String URL = "jdbc:mysql://localhost:3306/Wime";
    private final String USER = "root";
    private final String PASSWORD = "";

    @GetMapping("/api/estadisticas")
    public Map<String, Object> obtenerEstadisticas(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // ✅ Verificar sesión activa
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            response.put("success", false);
            response.put("message", "⚠️ Sesión no iniciada");
            return response;
        }

        int usuarioID = usuario.getIdUsuario();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // 📌 Tareas completadas
            String sqlTareas = "SELECT COUNT(*) AS total FROM tareas WHERE id_usuario = ? AND estado = 'completada'";
            try (PreparedStatement stmt = conn.prepareStatement(sqlTareas)) {
                stmt.setInt(1, usuarioID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    response.put("tareas_completadas", rs.getInt("total"));
                }
            }

            // 📌 Rutinas finalizadas
            String sqlRutinas = "SELECT COUNT(*) AS total FROM rutinas WHERE IDusuarios = ? AND Estado = 'completada'";
            try (PreparedStatement stmt = conn.prepareStatement(sqlRutinas)) {
                stmt.setInt(1, usuarioID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    response.put("rutinas_finalizadas", rs.getInt("total"));
                }
            }

            // 📌 En proceso (suma de tareas y rutinas)
            String sqlEnProceso = """
                SELECT 
                  (SELECT COUNT(*) FROM tareas WHERE id_usuario = ? AND estado = 'en progreso') +
                  (SELECT COUNT(*) FROM rutinas WHERE IDusuarios = ? AND Estado = 'en progreso') AS total
                """;
            try (PreparedStatement stmt = conn.prepareStatement(sqlEnProceso)) {
                stmt.setInt(1, usuarioID);
                stmt.setInt(2, usuarioID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    response.put("en_proceso", rs.getInt("total"));
                }
            }

            response.put("success", true);

        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "❌ Error de conexión a la base de datos");
            response.put("error", e.getMessage());
        }

        return response;
    }
}
