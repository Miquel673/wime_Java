package com.example.Wime_java.Admin.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Wime_java.Admin.dto.AdminNotificacionDTO;
import com.example.Wime_java.model.Notificacion;
import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.NotificacionRepository;
import com.example.Wime_java.repository.UsuarioRepository;
import com.example.Wime_java.service.NotificacionService;
import com.example.Wime_java.service.PdfGeneratorService;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

public List<AdminNotificacionDTO> listarNotificaciones() {
        Map<Long, String> nombresPorUsuario = usuarioRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Usuario::getIdUsuario,
                        usuario -> usuario.getNombreUsuario() != null && !usuario.getNombreUsuario().isBlank()
                                ? usuario.getNombreUsuario()
                                : usuario.getEmailUsuario(),
                        (actual, reemplazo) -> actual));

        return notificacionRepository.findAllByOrderByFechaDesc().stream()
                .map(notificacion -> new AdminNotificacionDTO(
                        notificacion.getId(),
                        notificacion.getIdUsuario(),
                        nombresPorUsuario.getOrDefault(notificacion.getIdUsuario(), "Usuario no disponible"),
                        notificacion.getTipo(),
                        notificacion.getMensaje(),
                        notificacion.getFecha(),
                        notificacion.isLeida()))
                .toList();
    }

    public void eliminarNotificacion(Long idNotificacion) {
        notificacionRepository.deleteById(idNotificacion);
    }

    public void eliminarTodasLasNotificaciones() {
        notificacionRepository.deleteAllInBatch();
    }

    public void cambiarEstado(Integer id, String nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setEstado(nuevoEstado);
        usuarioRepository.save(usuario);

        String mensaje = "Se ha " + ("Activo".equalsIgnoreCase(nuevoEstado) ? "reactivado" : "inactivado")
                + " al usuario " + usuario.getNombreUsuario() + " (" + usuario.getEmailUsuario() + ").";
        notificarAdministradores(mensaje);
    }

    public void cambiarTipo(Integer id, String nuevoTipo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setTipo(nuevoTipo);
        usuarioRepository.save(usuario);

        if (nuevoTipo != null && nuevoTipo.toLowerCase().contains("admin")) {
            notificarAdministradores("Se ha delegado a " + usuario.getNombreUsuario() + " (" + usuario.getEmailUsuario() + ") como administrador.");
        }
    }

    public void eliminarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar. Usuario no encontrado con ID: " + id));

        if (usuario.getTipo() != null && usuario.getTipo().toLowerCase().contains("admin")) {
            throw new IllegalStateException("No se permite eliminar usuarios administradores.");
        }

        String nombreUsuario = usuario.getNombreUsuario();
        String email = usuario.getEmailUsuario();
        usuarioRepository.deleteById(id);

        notificarAdministradores("El usuario " + nombreUsuario + " (" + email + ") ha eliminado su cuenta Wime.");
        notificarAdministradores("Se ha eliminado al usuario " + nombreUsuario + " (" + email + ") desde administración.");
    }

    public ByteArrayInputStream generarReporteAdminPdf(Long idAdmin) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Notificacion> notificaciones = notificacionRepository.findByIdUsuarioOrderByFechaDesc(idAdmin);

        Map<String, Long> dataGrafico = usuarios.stream()
                .collect(Collectors.groupingBy(
                        u -> {
                            LocalDateTime ultimo = u.getUltimoLogin();
                            return ultimo != null ? ultimo.toLocalDate().toString() : "Nunca";
                        },
                        LinkedHashMap::new,
                        Collectors.counting()));

        return pdfGeneratorService.generarReporteAdmin(usuarios, notificaciones, dataGrafico);
    }

    private void notificarAdministradores(String mensaje) {
        List<Usuario> admins = usuarioRepository.findAll().stream()
                .filter(u -> u.getTipo() != null && u.getTipo().toLowerCase().contains("admin"))
                .toList();

        for (Usuario admin : admins) {
            notificacionService.crearNotificacion(admin.getIdUsuario(), "Admin", mensaje);
        }
    }
}