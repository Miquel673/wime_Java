package com.example.Wime_java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Wime_java.model.Notificacion;
import com.example.Wime_java.repository.NotificacionRepository;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    // Obtener todas las notificaciones de un usuario
    public List<Notificacion> obtenerPorUsuario(Long idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo.");
        }
        return notificacionRepository.findByIdUsuario(idUsuario);
    }

    // Crear una nueva notificación
    public Notificacion crearNotificacion(Long idUsuario, String tipo, String mensaje) {
        if (idUsuario == null || tipo == null || mensaje == null) {
            throw new IllegalArgumentException("Los campos idUsuario, tipo y mensaje son obligatorios.");
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setIdUsuario(idUsuario);
        notificacion.setTipo(tipo);
        notificacion.setMensaje(mensaje);
        notificacion.setLeida(false);

        return notificacionRepository.save(notificacion);
    }

    // Marcar todas como leídas
    @Transactional
    public void marcarLeidas(Long idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo.");
        }

        List<Notificacion> notificaciones = notificacionRepository.findByIdUsuario(idUsuario);

        if (notificaciones.isEmpty()) {
            throw new IllegalStateException("No se encontraron notificaciones para el usuario ID: " + idUsuario);
        }

        for (Notificacion n : notificaciones) {
            n.setLeida(true);
        }

        notificacionRepository.saveAll(notificaciones);
    }

    // Eliminar todas las notificaciones del usuario
    @Transactional
    public void eliminarTodas(Long idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo.");
        }

        List<Notificacion> notificaciones = notificacionRepository.findByIdUsuario(idUsuario);

        if (notificaciones.isEmpty()) {
            throw new IllegalStateException("No hay notificaciones para eliminar para el usuario ID: " + idUsuario);
        }

        notificacionRepository.deleteAll(notificaciones);
    }
}
