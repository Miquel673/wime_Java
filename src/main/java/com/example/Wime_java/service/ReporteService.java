package com.example.Wime_java.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Wime_java.model.Rutina;
import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.repository.RutinaRepository;
import com.example.Wime_java.repository.TareaRepository;

@Service
public class ReporteService {

    private final TareaRepository tareaRepository;
    private final RutinaRepository rutinaRepository;
    private final PdfGeneratorService pdfGeneratorService;

    public ReporteService(
            TareaRepository tareaRepository,
            RutinaRepository rutinaRepository,
            PdfGeneratorService pdfGeneratorService) {
        this.tareaRepository = tareaRepository;
        this.rutinaRepository = rutinaRepository;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    // ✅ Reporte de Tareas por usuario
    // ✅ Reporte de Tareas por usuario
public ByteArrayInputStream generarReporteTareas(Long idUsuario) {
    List<Tarea> tareas = tareaRepository.findByIdUsuario(idUsuario);
    if (tareas == null || tareas.isEmpty()) {
        // en vez de romper con excepción, devolvemos un PDF vacío con mensaje
        return pdfGeneratorService.generarMensaje("No hay tareas registradas para este usuario.");
    }
    return pdfGeneratorService.generarReporteTareas(tareas);
}

// ✅ Reporte de Rutinas por usuario
public ByteArrayInputStream generarReporteRutinas(Long idUsuario) {
    List<Rutina> rutinas = rutinaRepository.findByIdUsuario(idUsuario);
    if (rutinas == null || rutinas.isEmpty()) {
        return pdfGeneratorService.generarMensaje("No hay rutinas registradas para este usuario.");
    }
    return pdfGeneratorService.generarReporteRutinas(rutinas);
}
}
