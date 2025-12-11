package com.example.Wime_java.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Wime_java.dto.EstadisticasReporte;
import com.example.Wime_java.model.Rutina;
import com.example.Wime_java.model.Tarea;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

@Service
public class PdfGeneratorService {

    // 📊 Reporte de estadísticas globales
    public ByteArrayInputStream generarpdf(List<Tarea> tareas) {
    try {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.add(new Paragraph("📄 Reporte de Tareas")
                .setFont(font)
                .setBold()
                .setFontSize(18));

        float[] columnWidths = {150F, 100F, 100F, 120F};
        Table table = new Table(columnWidths);

        // Encabezados
        table.addCell("Título");
        table.addCell("Prioridad");
        table.addCell("Estado");
        table.addCell("Fecha límite");

        // Datos
        for (Tarea tarea : tareas) {
            table.addCell(tarea.getTitulo() != null ? tarea.getTitulo() : "N/A");
            table.addCell(tarea.getPrioridad() != null ? tarea.getPrioridad() : "N/A");
            table.addCell(tarea.getEstado() != null ? tarea.getEstado() : "N/A");

            String fecha = tarea.getFechaLimite() != null
                    ? tarea.getFechaLimite().toString()
                    : "N/A";

            table.addCell(fecha);
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    } catch (Exception e) {
        throw new RuntimeException("❌ Error al generar PDF: " + e.getMessage(), e);
    }
}


    // ✅ Reporte detallado de Tareas
    public ByteArrayInputStream generarReporteTareas(List<Tarea> tareas) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            document.add(new Paragraph(" Reporte de Tareas")
                    .setFont(font)
                    .setBold()
                    .setFontSize(18));

            float[] columnWidths = {150F, 100F, 100F, 120F};
            Table table = new Table(columnWidths);

            // Encabezados
            table.addCell("Título");
            table.addCell("Prioridad");
            table.addCell("Estado");
            table.addCell("Fecha límite");

            // Datos
            for (Tarea tarea : tareas) {
                table.addCell(tarea.getTitulo() != null ? tarea.getTitulo() : "N/A");
                table.addCell(tarea.getPrioridad() != null ? tarea.getPrioridad() : "N/A");
                table.addCell(tarea.getEstado() != null ? tarea.getEstado() : "N/A");
                table.addCell(tarea.getFechaLimite() != null ? tarea.getFechaLimite().toString() : "N/A");
            }

            document.add(table);
            document.close();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("❌ Error al generar PDF de tareas: " + e.getMessage(), e);
        }
    }

    public ByteArrayInputStream generarMensaje(String mensaje) {
    try {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph(mensaje).setFontSize(14));
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    } catch (Exception e) {
        throw new RuntimeException("Error al generar PDF de mensaje: " + e.getMessage(), e);
    }
}


    // ✅ Reporte detallado de Rutinas
    public ByteArrayInputStream generarReporteRutinas(List<Rutina> rutinas) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            document.add(new Paragraph(" Reporte de Rutinas")
                    .setFont(font)
                    .setBold()
                    .setFontSize(18));

            float[] columnWidths = {150F, 100F, 100F, 120F};
            Table table = new Table(columnWidths);

            // Encabezados
            table.addCell("Nombre");
            table.addCell("Prioridad");
            table.addCell("Estado");
            table.addCell("Fecha fin");

            // Datos
            for (Rutina rutina : rutinas) {
                table.addCell(rutina.getNombreRutina() != null ? rutina.getNombreRutina() : "N/A");
                table.addCell(rutina.getPrioridad() != null ? rutina.getPrioridad() : "N/A");
                table.addCell(rutina.getEstado() != null ? rutina.getEstado() : "N/A");
                table.addCell(rutina.getFechaFin() != null ? rutina.getFechaFin().toString() : "N/A");
            }

            document.add(table);
            document.close();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("❌ Error al generar PDF de rutinas: " + e.getMessage(), e);
        }
    }
}
