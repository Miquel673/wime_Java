package com.example.Wime_java.controller;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.example.Wime_java.service.ReporteService;

import jakarta.servlet.http.HttpSession;

class ReporteControllerTest {

    @InjectMocks
    private ReporteController controller;

    @Mock
    private ReporteService reporteService;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================
    // ✅ REPORTE TAREAS
    // =========================
    @Test
    void testGenerarReporteTareas_SinSesion() {
        when(session.getAttribute("id_usuario")).thenReturn(null);

        ResponseEntity<Resource> response = controller.generarReporteTareas(session);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testGenerarReporteTareas_Exitoso() {
        Long idUsuario = 1L;

        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);

        ByteArrayInputStream mockStream =
                new ByteArrayInputStream("PDF DATA".getBytes());

        when(reporteService.generarReporteTareas(idUsuario))
                .thenReturn(mockStream);

        ResponseEntity<Resource> response =
                controller.generarReporteTareas(session);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        String header = response.getHeaders()
                .getFirst("Content-Disposition");

        assertTrue(header.contains("tareas_" + idUsuario + ".pdf"));
    }

    // =========================
    // ✅ REPORTE RUTINAS
    // =========================
    @Test
    void testGenerarReporteRutinas_SinSesion() {
        when(session.getAttribute("id_usuario")).thenReturn(null);

        ResponseEntity<Resource> response =
                controller.generarReporteRutinas(session);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testGenerarReporteRutinas_Exitoso() {
        Long idUsuario = 1L;

        when(session.getAttribute("id_usuario")).thenReturn(idUsuario);

        ByteArrayInputStream mockStream =
                new ByteArrayInputStream("PDF DATA".getBytes());

        when(reporteService.generarReporteRutinas(idUsuario))
                .thenReturn(mockStream);

        ResponseEntity<Resource> response =
                controller.generarReporteRutinas(session);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        String header = response.getHeaders()
                .getFirst("Content-Disposition");

        assertTrue(header.contains("rutinas_" + idUsuario + ".pdf"));
    }
}