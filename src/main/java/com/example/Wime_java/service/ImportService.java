package com.example.Wime_java.service;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Wime_java.model.Rutina;
import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.repository.RutinaRepository;
import com.example.Wime_java.repository.TareaRepository;
import com.opencsv.CSVReader;


@Service
public class ImportService {



    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

public void importarDatos(String tipo, MultipartFile file, Long idUsuario) {

    try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {

        List<String[]> filas = reader.readAll();

        if (filas.size() <= 1) {
            throw new RuntimeException("El CSV no tiene datos válidos.");
        }

        for (int i = 1; i < filas.size(); i++) {

            String[] fila = filas.get(i);
            if (fila.length < 4) continue;

            String nombre = fila[0];
            String prioridad = fila[1];
            String fecha = fila[2];
            String estado = fila[3];

            if (tipo.equalsIgnoreCase("tareas")) {

                Tarea t = new Tarea();
                t.setIdUsuario(idUsuario);  // ← IMPORTANTE
                t.setTitulo(nombre);
                t.setPrioridad(prioridad);
                t.setEstado(estado);

                if (!fecha.isBlank()) {
                    t.setFechaLimite(LocalDate.parse(fecha, formatter));
                }

                tareaRepository.save(t);

            } else if (tipo.equalsIgnoreCase("rutinas")) {

                Rutina r = new Rutina();
                r.setIdUsuario(idUsuario); // ← IMPORTANTE
                r.setNombreRutina(nombre);
                r.setPrioridad(prioridad);
                r.setEstado(estado);

                if (!fecha.isBlank()) {
                    r.setFechaFin(LocalDate.parse(fecha, formatter));
                }

                rutinaRepository.save(r);

            } else {
                throw new IllegalArgumentException("Tipo de importación no válido: " + tipo);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error importando CSV: " + e.getMessage());
    }
    }
}