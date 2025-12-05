package com.example.Wime_java.controller;

import java.io.ByteArrayInputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Wime_java.service.ReporteService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/tareas")
public ResponseEntity<Resource> generarReporteTareas(HttpSession session) {
    Long idUsuario = (Long) session.getAttribute("id_usuario"); // 👈 mismo nombre que en login
    if (idUsuario == null) {
        return ResponseEntity.badRequest().build();
    }

    ByteArrayInputStream bis = reporteService.generarReporteTareas(idUsuario);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tareas_" + idUsuario + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(bis));
}


    @GetMapping("/rutinas")
public ResponseEntity<Resource> generarReporteRutinas(HttpSession session) {
    Long idUsuario = (Long) session.getAttribute("id_usuario"); // 👈 igual que en tareas
    if (idUsuario == null) {
        return ResponseEntity.badRequest().build();
    }

    ByteArrayInputStream bis = reporteService.generarReporteRutinas(idUsuario);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rutinas_" + idUsuario + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(bis));
}

    }

