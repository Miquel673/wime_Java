package com.example.Wime_java.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.repository.TareaCompartidaRepository;
import com.example.Wime_java.repository.TareaRepository;
import com.example.Wime_java.repository.UsuarioRepository;
import com.example.Wime_java.service.EmailService;
import com.example.Wime_java.service.NotificacionService;
import com.example.Wime_java.service.TareaService;

import jakarta.servlet.http.HttpSession;

class TareaControllerTest {

    @InjectMocks
    private TareaController controller;

    @Mock
    private TareaRepository tareaRepository;

    @Mock
    private TareaService tareaService;

    @Mock
    private NotificacionService notificacionService;

    @Mock
    private EmailService emailService;

    @Mock
    private TareaCompartidaRepository tareaCompartidaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================
    // 🔹 CAMBIAR ESTADO
    // =========================
    @Test
    void testCambiarEstadoExitoso() {

        Long idUsuario = 1L;
        Long idTarea = 10L;

        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);

        Tarea tarea = new Tarea();
        tarea.setIdUsuario(idUsuario);
        tarea.setTitulo("Tarea prueba");

        when(tareaRepository.findById(idTarea))
                .thenReturn(Optional.of(tarea));

        when(tareaService.actualizarEstado(idTarea, "COMPLETADA"))
                .thenReturn(tarea);

        Map<String, String> body = Map.of("estado", "COMPLETADA");

        ResponseEntity<?> response =
                controller.cambiarEstadoTarea(idTarea, body, session);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCambiarEstadoSinSesion() {

        when(session.getAttribute("id_usuario")).thenReturn(null);

        ResponseEntity<?> response =
                controller.cambiarEstadoTarea(1L, Map.of("estado", "OK"), session);

        assertEquals(401, response.getStatusCodeValue());
    }

    // =========================
    // 🔹 CREAR TAREA
    // =========================
    @Test
    void testCrearTareaExitoso() {

        Long idUsuario = 1L;
        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);

        Map<String, Object> datos = new HashMap<>();
        datos.put("titulo", "Nueva tarea");
        datos.put("prioridad", "Alta");
        datos.put("descripcion", "Descripción");

        Tarea tareaGuardada = new Tarea();
        tareaGuardada.setIdTarea(1L);
        tareaGuardada.setTitulo("Nueva tarea");

        when(tareaService.guardarTarea(any()))
                .thenReturn(tareaGuardada);

        ResponseEntity<?> response =
                controller.guardarTarea(datos, session);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCrearTareaSinSesion() {

        when(session.getAttribute("id_usuario")).thenReturn(null);

        ResponseEntity<?> response =
                controller.guardarTarea(new HashMap<>(), session);

        assertEquals(401, response.getStatusCodeValue());
    }

    // =========================
    // 🔹 OBTENER TAREA
    // =========================
    @Test
    void testObtenerTareaExitoso() {

        Long idUsuario = 1L;
        Long idTarea = 10L;

        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);

        Tarea tarea = new Tarea();
        tarea.setIdUsuario(idUsuario);

        when(tareaRepository.findById(idTarea))
                .thenReturn(Optional.of(tarea));

        ResponseEntity<?> response =
                controller.obtenerTareaPorId(idTarea, session);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testObtenerTareaNoExiste() {

        when(session.getAttribute("id_usuario")).thenReturn(1L);

        when(tareaRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response =
                controller.obtenerTareaPorId(1L, session);

        assertEquals(404, response.getStatusCodeValue());
    }

    // =========================
    // 🔹 ELIMINAR TAREA
    // =========================
    @Test
    void testEliminarTareaExitoso() {

        Long idUsuario = 1L;
        Long idTarea = 10L;

        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);

        Tarea tarea = new Tarea();
        tarea.setIdUsuario(idUsuario);
        tarea.setTitulo("Eliminar");

        when(tareaRepository.findById(idTarea))
                .thenReturn(Optional.of(tarea));

        ResponseEntity<?> response =
                controller.eliminarTarea(idTarea, session);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testEliminarTareaNoAutorizado() {

        when(session.getAttribute("id_usuario")).thenReturn(null);

        ResponseEntity<?> response =
                controller.eliminarTarea(1L, session);

        assertEquals(401, response.getStatusCodeValue());
    }

    // =========================
    // 🔹 REMOVER COMPARTIDA
    // =========================
    @Test
    void testRemoverCompartidaSinSesion() {

        when(session.getAttribute("id_usuario")).thenReturn(null);

        ResponseEntity<?> response =
                controller.removerCompartida(1L, session);

        assertEquals(401, response.getStatusCodeValue());
    }
}