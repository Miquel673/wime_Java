package com.example.Wime_java.service;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Wime_java.model.Rutina;
import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.repository.RutinaRepository;
import com.example.Wime_java.repository.TareaRepository;
import com.opencsv.CSVWriter;

@Service
public class ExportService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    public ByteArrayInputStream exportarDatos(String tipo, Long idUsuario) {

        try (StringWriter writer = new StringWriter();
             CSVWriter csvWriter = new CSVWriter(writer)) {

            csvWriter.writeNext(new String[]{"Nombre", "Prioridad", "Fecha", "Estado"});

            if (tipo.equalsIgnoreCase("tareas")) {

                List<Tarea> tareas = tareaRepository.findByIdUsuario(idUsuario);

                for (Tarea t : tareas) {
                    csvWriter.writeNext(new String[]{
                            t.getTitulo(),
                            t.getPrioridad(),
                            t.getFechaLimite() != null ? t.getFechaLimite().toString() : "",
                            t.getEstado()
                    });
                }

            } else if (tipo.equalsIgnoreCase("rutinas")) {

                List<Rutina> rutinas = rutinaRepository.findByIdUsuario(idUsuario);

                for (Rutina r : rutinas) {
                    csvWriter.writeNext(new String[]{
                            r.getNombreRutina(),
                            r.getPrioridad(),
                            r.getFechaFin() != null ? r.getFechaFin().toString() : "",
                            r.getEstado()
                    });
                }

            } else {
                throw new IllegalArgumentException("Tipo de exportación no válido: " + tipo);
            }

            return new ByteArrayInputStream(writer.toString().getBytes());

        } catch (Exception e) {
            throw new RuntimeException("Error generando CSV: " + e.getMessage());
        }
    }
}
