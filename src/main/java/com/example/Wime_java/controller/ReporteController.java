package com.example.Wime_java.controller;

import java.io.ByteArrayInputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Wime_java.dto.EstadisticasReporte;
import com.example.Wime_java.service.PdfGeneratorService;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final PdfGeneratorService pdfService;

    public ReporteController(PdfGeneratorService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/estadisticas/pdf")
    public ResponseEntity<byte[]> generarReportePdf() {
        // 🔹 Aquí deberías obtener datos reales desde tu servicio/repositorio
        EstadisticasReporte reporte = new EstadisticasReporte();
        reporte.setTareasCompletadas(10);
        reporte.setTareasEnProceso(5);
        reporte.setTareasPendientes(2);
        reporte.setRutinasCompletadas(7);
        reporte.setRutinasEnProceso(3);
        reporte.setRutinasPendientes(1);

        ByteArrayInputStream bis = pdfService.generarPdf(reporte);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }
}
