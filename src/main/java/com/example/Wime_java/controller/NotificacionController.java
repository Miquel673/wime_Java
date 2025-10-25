package com.example.Wime_java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Wime_java.model.Notificacion;
import com.example.Wime_java.service.NotificacionService;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "http://localhost:5500") // ajusta el puerto si usas otro en frontend
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    // ✅ Obtener todas las notificaciones de un usuario
    @GetMapping("/{idUsuario}")
    public List<Notificacion> obtenerNotificaciones(@PathVariable Long idUsuario) {
        return notificacionService.obtenerPorUsuario(idUsuario);
    }

    // ✅ Crear una nueva notificación
    @PostMapping("/crear")
    public Notificacion crearNotificacion(
            @RequestParam Long idUsuario,
            @RequestParam String tipo,
            @RequestParam String mensaje) {
        return notificacionService.crearNotificacion(idUsuario, tipo, mensaje);
    }

    // ✅ Marcar todas las notificaciones como leídas
    @PutMapping("/{idUsuario}/marcar-leidas")
    public String marcarLeidas(@PathVariable Long idUsuario) {
        try {
            notificacionService.marcarLeidas(idUsuario);
            return "✅ Notificaciones marcadas como leídas";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error al marcar como leídas: " + e.getMessage();
        }
    }

    // ✅ Eliminar todas las notificaciones de un usuario
    @DeleteMapping("/{idUsuario}/eliminar-todas")
    public String eliminarTodas(@PathVariable Long idUsuario) {
        try {
            notificacionService.eliminarTodas(idUsuario);
            return "🗑️ Todas las notificaciones eliminadas";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error al eliminar notificaciones: " + e.getMessage();
        }
    }
}
