package com.example.Wime_java.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.example.Wime_java.dto.EstadisticasReporte;
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

    public ByteArrayInputStream generarPdf(EstadisticasReporte reporte) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Título
            document.add(new Paragraph("Reporte de Estadísticas")
                    .setFont(font)
                    .setBold()
                    .setFontSize(18));

            // Tabla de estadísticas
            float[] columnWidths = {200F, 100F};
            Table table = new Table(columnWidths);

            // Tareas
            table.addCell("Tareas Completadas");
            table.addCell(String.valueOf(reporte.getTareasCompletadas()));

            table.addCell("Tareas en Proceso");
            table.addCell(String.valueOf(reporte.getTareasEnProceso()));

            table.addCell("Tareas Pendientes");
            table.addCell(String.valueOf(reporte.getTareasPendientes()));

            // Rutinas
            table.addCell("Rutinas Completadas");
            table.addCell(String.valueOf(reporte.getRutinasCompletadas()));

            table.addCell("Rutinas en Proceso");
            table.addCell(String.valueOf(reporte.getRutinasEnProceso()));

            table.addCell("Rutinas Pendientes");
            table.addCell(String.valueOf(reporte.getRutinasPendientes()));

            document.add(table);

            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }
    }
}
