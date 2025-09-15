package com.wime.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication; // ✅ Import correcto
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SpringBootApplication
@RestController
public class Notificacion {

    public static void main(String[] args) {
        SpringApplication.run(Notificacion.class, args);
    }

    @PostMapping("/notificaciones/accion")
    public void gestionarAccion(
            @RequestParam("accion") String accion,
            HttpSession session,
            HttpServletResponse response) throws IOException {

        // Validar sesión
        Integer idUsuario = (Integer) session.getAttribute("id_usuario");
        if (idUsuario == null) {
            response.sendRedirect("/Wime/public/HTML/Wime_interfaz_Inicio-Sesion.html");
            return;
        }

        // Conexión a base de datos
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Wime", "root", "")) {

            if ("marcar_leidas".equals(accion)) {
                String sql = "UPDATE notificaciones SET leida = 1 WHERE id_usuario = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idUsuario);
                    stmt.executeUpdate();
                }
            }

            if ("eliminar_todas".equals(accion)) {
                String sql = "DELETE FROM notificaciones WHERE id_usuario = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idUsuario);
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // en producción deberías registrar el error de forma más segura
        }

        // Redirigir como en PHP
        response.sendRedirect("/Wime/private/PhP/Wime_interfaz_BandejaEntrada.php");
    }
}
