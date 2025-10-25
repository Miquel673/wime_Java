package com.example.Wime_java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Wime_java.model.Rutina;
import com.example.Wime_java.repository.RutinaRepository;

@Service
public class RutinaService {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private NotificacionService notificacionService; // 👈 Se inyecta el servicio de notificaciones

    // ✅ Guardar nueva rutina + crear notificación automática
    public Rutina guardarRutina(Rutina rutina) {
        Rutina nuevaRutina = rutinaRepository.save(rutina);

        try {
            String mensaje = "Se ha creado una nueva rutina: " + rutina.getNombreRutina();
            notificacionService.crearNotificacion(rutina.getIdUsuario(), "Rutina", mensaje);
        } catch (Exception e) {
            System.err.println("⚠️ Error al crear notificación para la rutina: " + e.getMessage());
        }

        return nuevaRutina;
    }

    // ✅ Listar rutinas por usuario
    public List<Rutina> listarPorUsuario(Long idUsuario) {
        return rutinaRepository.findByIdUsuario(idUsuario);
    }

    // ✅ Obtener rutina por ID
    public Optional<Rutina> obtenerPorId(Long idRutina) {
        return rutinaRepository.findById(idRutina);
    }

    // ✅ Editar rutina existente
    public Rutina editarRutina(Rutina rutina) {
        Rutina actualizada = rutinaRepository.save(rutina);

        try {
            String mensaje = "Se ha actualizado la rutina: " + rutina.getNombreRutina();
            notificacionService.crearNotificacion(rutina.getIdUsuario(), "Rutina", mensaje);
        } catch (Exception e) {
            System.err.println("⚠️ Error al crear notificación de edición: " + e.getMessage());
        }

        return actualizada;
    }

    // ✅ Eliminar rutina
    public void eliminarRutina(Long idRutina) {
        Optional<Rutina> rutinaOpt = rutinaRepository.findById(idRutina);

        if (rutinaOpt.isPresent()) {
            Rutina rutina = rutinaOpt.get();
            rutinaRepository.deleteById(idRutina);

            try {
                String mensaje = "Se ha eliminado la rutina: " + rutina.getNombreRutina();
                notificacionService.crearNotificacion(rutina.getIdUsuario(), "Rutina", mensaje);
            } catch (Exception e) {
                System.err.println("⚠️ Error al crear notificación de eliminación: " + e.getMessage());
            }
        }
    }
}
