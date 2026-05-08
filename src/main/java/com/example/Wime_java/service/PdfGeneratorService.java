package com.example.Wime_java.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.Wime_java.model.Notificacion;
import com.example.Wime_java.model.Rutina;
import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.model.Usuario;
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
            table.addCell("Título");
            table.addCell("Prioridad");
            table.addCell("Estado");
            table.addCell("Fecha límite");

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
            throw new RuntimeException(" Error al generar PDF: " + e.getMessage(), e);
        }
    }

    public ByteArrayInputStream generarReporteTareas(List<Tarea> tareas) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            document.add(new Paragraph("Reporte de Tareas")
                    .setFont(font)
                    .setBold()
                    .setFontSize(18));

            float[] columnWidths = {150F, 100F, 100F, 120F};
            Table table = new Table(columnWidths);
            table.addCell("Título");
            table.addCell("Prioridad");
            table.addCell("Estado");
            table.addCell("Fecha límite");

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
            throw new RuntimeException(" Error al generar PDF de tareas: " + e.getMessage(), e);
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

    public ByteArrayInputStream generarReporteRutinas(List<Rutina> rutinas) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            document.add(new Paragraph("Reporte de Rutinas")
                    .setFont(font)
                    .setBold()
                    .setFontSize(18));

            float[] columnWidths = {150F, 100F, 100F, 120F};
            Table table = new Table(columnWidths);
            table.addCell("Nombre");
            table.addCell("Prioridad");
            table.addCell("Estado");
            table.addCell("Fecha fin");

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
            throw new RuntimeException(" Error al generar PDF de rutinas: " + e.getMessage(), e);
        }
    }

    public ByteArrayInputStream generarReporteAdmin(
            List<Usuario> usuarios,
            List<Notificacion> notificaciones,
            Map<String, Long> dataGraficoUltimoLogin) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            document.add(new Paragraph("Reporte Administrativo WIME")
                    .setFont(font)
                    .setBold()
                    .setFontSize(18));

            document.add(new Paragraph("1) Usuarios y estados").setBold().setMarginTop(15));
            Table tablaUsuarios = new Table(new float[]{70F, 170F, 170F, 100F});
            tablaUsuarios.addCell("ID");
            tablaUsuarios.addCell("Usuario");
            tablaUsuarios.addCell("Email");
            tablaUsuarios.addCell("Estado");
            for (Usuario usuario : usuarios) {
                tablaUsuarios.addCell(String.valueOf(usuario.getIdUsuario()));
                tablaUsuarios.addCell(usuario.getNombreUsuario() != null ? usuario.getNombreUsuario() : "N/A");
                tablaUsuarios.addCell(usuario.getEmailUsuario() != null ? usuario.getEmailUsuario() : "N/A");
                tablaUsuarios.addCell(usuario.getEstado() != null ? usuario.getEstado() : "N/A");
            }
            document.add(tablaUsuarios);

            document.add(new Paragraph("2) Notificaciones").setBold().setMarginTop(15));
            Table tablaNotificaciones = new Table(new float[]{110F, 330F});
            tablaNotificaciones.addCell("Fecha");
            tablaNotificaciones.addCell("Mensaje");
            if (notificaciones.isEmpty()) {
                tablaNotificaciones.addCell("-");
                tablaNotificaciones.addCell("No hay notificaciones registradas.");
            } else {
                for (Notificacion n : notificaciones) {
                    tablaNotificaciones.addCell(n.getFecha() != null ? n.getFecha().toString() : "N/A");
                    tablaNotificaciones.addCell(n.getMensaje() != null ? n.getMensaje() : "N/A");
                }
            }
            document.add(tablaNotificaciones);

            document.add(new Paragraph("3) Datos de gráfico (Último_Login)").setBold().setMarginTop(15));
            Table tablaGrafico = new Table(new float[]{250F, 190F});
            tablaGrafico.addCell("Fecha último login");
            tablaGrafico.addCell("Cantidad usuarios");
            if (dataGraficoUltimoLogin.isEmpty()) {
                tablaGrafico.addCell("N/A");
                tablaGrafico.addCell("0");
            } else {
                for (Map.Entry<String, Long> entry : dataGraficoUltimoLogin.entrySet()) {
                    tablaGrafico.addCell(entry.getKey());
                    tablaGrafico.addCell(String.valueOf(entry.getValue()));
                }
            }
            document.add(tablaGrafico);

            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte administrativo PDF: " + e.getMessage(), e);
        }
    }
}