package com.example.Wime_java.controller;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.Wime_java.model.Rutina;
import com.example.Wime_java.repository.RutinaCompartidaRepository;
import com.example.Wime_java.repository.RutinaRepository;
import com.example.Wime_java.repository.UsuarioRepository;
import com.example.Wime_java.service.EmailService;
import com.example.Wime_java.service.NotificacionService;
import com.example.Wime_java.service.RutinaService;

import jakarta.servlet.http.HttpSession;

class RutinaControllerTest {

    @InjectMocks
    private RutinaController controller;

    @Mock
    private RutinaRepository rutinaRepository;

    @Mock
    private RutinaService rutinaService;

    @Mock
    private NotificacionService notificacionService;

    @Mock
    private RutinaCompartidaRepository rutinaCompartidaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================
    // ✅ CAMBIAR ESTADO
    // =========================
    @Test
    void testCambiarEstadoRutina_SinSesion() {
        when(session.getAttribute("id_usuario")).thenReturn(null);

        ResponseEntity<?> response = controller.cambiarEstadoRutina(1L, Map.of("estado", "completada"), session);

        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void testCambiarEstadoRutina_Exitoso() {
        Long idUsuario = 1L;

        Rutina rutina = new Rutina();
        rutina.setIdUsuario(idUsuario);
        rutina.setNombreRutina("Ejercicio");

        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);
        when(rutinaRepository.findById(1L)).thenReturn(Optional.of(rutina));

        Rutina actualizada = new Rutina();
        actualizada.setEstado("completada");
        actualizada.setNombreRutina("Ejercicio");

        when(rutinaService.actualizarEstado(1L, "completada")).thenReturn(actualizada);

        ResponseEntity<?> response = controller.cambiarEstadoRutina(1L, Map.of("estado", "completada"), session);

        assertEquals(200, response.getStatusCodeValue());
    }

    // =========================
    // ✅ CREAR RUTINA
    // =========================
    @Test
    void testCrearRutina_SinSesion() {
        when(session.getAttribute("id_usuario")).thenReturn(null);

        ResponseEntity<?> response = controller.crearRutina(Map.of(), session);

        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void testCrearRutina_Exitoso() {
        Long idUsuario = 1L;

        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);

        Map<String, Object> datos = Map.of(
                "nombreRutina", "Leer",
                "fechaAsignacion", LocalDate.now().toString(),
                "fechaFin", LocalDate.now().plusDays(1).toString(),
                "prioridad", "media",
                "frecuencia", "diario"
        );

        Rutina rutinaGuardada = new Rutina();
        rutinaGuardada.setIdRutina(1L);

        when(rutinaService.guardarRutina(any())).thenReturn(rutinaGuardada);

        ResponseEntity<?> response = controller.crearRutina(datos, session);

        assertEquals(200, response.getStatusCodeValue());
    }

    // =========================
    // ✅ OBTENER RUTINA
    // =========================
    @Test
    void testObtenerRutina_NoEncontrada() {
        when(session.getAttribute("id_usuario")).thenReturn(1L);
        when(rutinaRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.obtenerRutina(1L, session);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testObtenerRutina_Exitoso() {
        Long idUsuario = 1L;

        Rutina rutina = new Rutina();
        rutina.setIdUsuario(idUsuario);

        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);
        when(rutinaRepository.findById(1L)).thenReturn(Optional.of(rutina));

        ResponseEntity<?> response = controller.obtenerRutina(1L, session);

        assertEquals(200, response.getStatusCodeValue());
    }

    // =========================
    // ✅ ELIMINAR RUTINA
    // =========================
    @Test
    void testEliminarRutina_SinSesion() {
        when(session.getAttribute("id_usuario")).thenReturn(null);

        Map<String, Object> response = controller.eliminarRutina(1L, session);

        assertFalse((Boolean) response.get("success"));
    }

    @Test
    void testEliminarRutina_Exitoso() {
        Long idUsuario = 1L;

        Rutina rutina = new Rutina();
        rutina.setIdUsuario(idUsuario);

        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);
        when(rutinaRepository.findById(1L)).thenReturn(Optional.of(rutina));

        Map<String, Object> response = controller.eliminarRutina(1L, session);

        assertTrue((Boolean) response.get("success"));
        verify(rutinaService).eliminarRutina(1L);
    }

    // =========================
    // ✅ COMPARTIR RUTINA
    // =========================
    @Test
    void testCompartirRutina_SinSesion() {
        when(session.getAttribute("id_usuario")).thenReturn(null);

        ResponseEntity<?> response = controller.compartirRutina(1L, Map.of("emails", "test@test.com"), session);

        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void testCompartirRutina_NoEncontrada() {
        when(session.getAttribute("id_usuario")).thenReturn(1L);
        when(rutinaRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.compartirRutina(1L, Map.of("emails", "test@test.com"), session);

        assertEquals(404, response.getStatusCodeValue());
    }

}