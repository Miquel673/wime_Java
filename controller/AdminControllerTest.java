package com.example.Wime_java.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import com.example.Wime_java.Admin.controller.AdminRestController;
import com.example.Wime_java.Admin.dto.AdminNotificacionDTO;
import com.example.Wime_java.Admin.service.AdminService;
import com.example.Wime_java.model.Usuario;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminRestController adminController;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
// Elimina esa línea si el ID es autogenerado  usuario.setNombreUsuario("Miguel");
        usuario.setEstado("activo");
        usuario.setTipo("usuario");
    }

    // 🔹 Listar usuarios
    @Test
    void listarUsuarios() {
        when(adminService.listarUsuarios()).thenReturn(List.of(usuario));

        ResponseEntity<List<Usuario>> response = adminController.listarUsuarios();

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    // 🔹 Listar notificaciones
    @Test
    void listarNotificaciones() {
        new AdminNotificacionDTO(
            1L,
            1L,
            "TIPO",
            "Mensaje",
            "Estado",
            java.time.LocalDateTime.now(),
            false
        );
        ResponseEntity<List<AdminNotificacionDTO>> response = adminController.listarNotificaciones();

        assertEquals(200, response.getStatusCodeValue());
    }

    // 🔹 Eliminar notificación
    @Test
    void eliminarNotificacion() {
        ResponseEntity<Void> response = adminController.eliminarNotificacion(1L);

        verify(adminService).eliminarNotificacion(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    // 🔹 Eliminar todas las notificaciones
    @Test
    void eliminarTodasNotificaciones() {
        ResponseEntity<Void> response = adminController.eliminarTodasLasNotificaciones();

        verify(adminService).eliminarTodasLasNotificaciones();
        assertEquals(204, response.getStatusCodeValue());
    }

    // 🔹 Cambiar estado
    @Test
    void cambiarEstado() {
        ResponseEntity<String> response = adminController.cambiarEstado(1, "activo");

        verify(adminService).cambiarEstado(1, "activo");
        assertEquals(200, response.getStatusCodeValue());
    }

    // 🔹 Cambiar tipo
    @Test
    void cambiarTipo() {
        ResponseEntity<String> response = adminController.cambiarTipo(1, "admin");

        verify(adminService).cambiarTipo(1, "admin");
        assertEquals(200, response.getStatusCodeValue());
    }

    // 🔹 Eliminar usuario (OK)
    @Test
    void eliminarUsuarioOK() {
        ResponseEntity<String> response = adminController.eliminarUsuario(1);

        verify(adminService).eliminarUsuario(1);
        assertEquals(200, response.getStatusCodeValue());
    }

    // 🔹 Eliminar usuario (error)
    @Test
    void eliminarUsuarioError() {
        doThrow(new IllegalStateException("No se puede eliminar"))
                .when(adminService).eliminarUsuario(1);

        ResponseEntity<String> response = adminController.eliminarUsuario(1);

        assertEquals(400, response.getStatusCodeValue());
    }

    // 🔹 Generar reporte con sesión
    @Test
    void generarReporteConSesion() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id_usuario", 1L);

        when(adminService.generarReporteAdminPdf(1L))
                .thenReturn(new ByteArrayInputStream(new byte[0]));

        ResponseEntity<?> response = adminController.exportarReporteAdmin(session);

        assertEquals(200, response.getStatusCodeValue());
    }

    // 🔹 Generar reporte sin sesión
    @Test
    void generarReporteSinSesion() {
        MockHttpSession session = new MockHttpSession();

        ResponseEntity<?> response = adminController.exportarReporteAdmin(session);

        assertEquals(401, response.getStatusCodeValue());
    }
}