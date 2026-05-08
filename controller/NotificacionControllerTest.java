package com.example.Wime_java.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.Wime_java.model.Notificacion;
import com.example.Wime_java.service.NotificacionService;

class NotificacionControllerTest {

    @InjectMocks
    private NotificacionController controller;

    @Mock
    private NotificacionService notificacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================
    // ✅ OBTENER NOTIFICACIONES
    // =========================
    @Test
    void testObtenerNotificaciones() {
        Long idUsuario = 1L;

        Notificacion notif = new Notificacion();
        notif.setMensaje("Nueva tarea creada");

        when(notificacionService.obtenerPorUsuario(idUsuario))
                .thenReturn(List.of(notif));

        List<Notificacion> resultado = controller.obtenerNotificaciones(idUsuario);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacionService).obtenerPorUsuario(idUsuario);
    }

    // =========================
    // ✅ CREAR NOTIFICACIÓN
    // =========================
    @Test
    void testCrearNotificacion() {
        Long idUsuario = 1L;

        Notificacion notif = new Notificacion();
        notif.setMensaje("Mensaje de prueba");

        when(notificacionService.crearNotificacion(idUsuario, "INFO", "Mensaje de prueba"))
                .thenReturn(notif);

        Notificacion resultado = controller.crearNotificacion(idUsuario, "INFO", "Mensaje de prueba");

        assertNotNull(resultado);
        assertEquals("Mensaje de prueba", resultado.getMensaje());
        verify(notificacionService).crearNotificacion(idUsuario, "INFO", "Mensaje de prueba");
    }

    // =========================
    // ✅ MARCAR COMO LEÍDAS
    // =========================
    @Test
    void testMarcarLeidas_Exitoso() {
        Long idUsuario = 1L;

        doNothing().when(notificacionService).marcarLeidas(idUsuario);

        String response = controller.marcarLeidas(idUsuario);

        assertEquals("Notificaciones marcadas como leídas", response);
        verify(notificacionService).marcarLeidas(idUsuario);
    }

    @Test
    void testMarcarLeidas_Error() {
        Long idUsuario = 1L;

        doThrow(new RuntimeException("Error interno"))
                .when(notificacionService).marcarLeidas(idUsuario);

        String response = controller.marcarLeidas(idUsuario);

        assertTrue(response.contains("Error al marcar como leídas"));
    }

    // =========================
    // ✅ ELIMINAR TODAS
    // =========================
    @Test
    void testEliminarTodas_Exitoso() {
        Long idUsuario = 1L;

        doNothing().when(notificacionService).eliminarTodas(idUsuario);

        String response = controller.eliminarTodas(idUsuario);

        assertEquals("Todas las notificaciones eliminadas", response);
        verify(notificacionService).eliminarTodas(idUsuario);
    }

    @Test
    void testEliminarTodas_Error() {
        Long idUsuario = 1L;

        doThrow(new RuntimeException("Error"))
                .when(notificacionService).eliminarTodas(idUsuario);

        String response = controller.eliminarTodas(idUsuario);

        assertTrue(response.contains("Error al eliminar notificaciones"));
    }

    // =========================
    // ✅ ELIMINAR UNA
    // =========================
    @Test
    void testEliminarNotificacion_Exitoso() {
        Long idUsuario = 1L;
        Long idNotificacion = 10L;

        doNothing().when(notificacionService)
                .eliminarNotificacion(idUsuario, idNotificacion);

        String response = controller.eliminarNotificacion(idUsuario, idNotificacion);

        assertEquals("Notificación eliminada", response);
        verify(notificacionService)
                .eliminarNotificacion(idUsuario, idNotificacion);
    }

    @Test
    void testEliminarNotificacion_Error() {
        Long idUsuario = 1L;
        Long idNotificacion = 10L;

        doThrow(new RuntimeException("Error"))
                .when(notificacionService)
                .eliminarNotificacion(idUsuario, idNotificacion);

        String response = controller.eliminarNotificacion(idUsuario, idNotificacion);

        assertTrue(response.contains("Error al eliminar notificación"));
    }
}