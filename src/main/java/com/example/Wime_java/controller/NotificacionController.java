package com.example.Wime_java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/{idUsuario}")
    public List<Notificacion> obtenerNotificaciones(@PathVariable Long idUsuario) {
        return notificacionService.obtenerPorUsuario(idUsuario);
    }

    @PostMapping("/crear")
    public Notificacion crearNotificacion(
            @RequestParam Long idUsuario,
            @RequestParam String tipo,
            @RequestParam String mensaje) {
        return notificacionService.crearNotificacion(idUsuario, tipo, mensaje);
    }

    @PutMapping("/{idUsuario}/marcar-leidas")
    public String marcarLeidas(@PathVariable Long idUsuario) {
        try {
            notificacionService.marcarLeidas(idUsuario);
            return "Notificaciones marcadas como leídas";
        } catch (Exception e) {
            return "Error al marcar como leídas: " + e.getMessage();
        }
    }

    @DeleteMapping("/{idUsuario}/eliminar-todas")
    public String eliminarTodas(@PathVariable Long idUsuario) {
        try {
            notificacionService.eliminarTodas(idUsuario);
            return "Todas las notificaciones eliminadas";
        } catch (Exception e) {
            return "Error al eliminar notificaciones: " + e.getMessage();
        }
    }

    @DeleteMapping("/{idUsuario}/{idNotificacion}")
    public String eliminarNotificacion(
            @PathVariable Long idUsuario,
            @PathVariable Long idNotificacion) {
        try {
            notificacionService.eliminarNotificacion(idUsuario, idNotificacion);
            return "Notificación eliminada";
        } catch (Exception e) {
            return "Error al eliminar notificación: " + e.getMessage();
        }
    }
}