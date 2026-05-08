package com.example.Wime_java.service;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Wime_java.model.Rutina;
import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.model.TareaCompartida;
import com.example.Wime_java.repository.RutinaRepository;
import com.example.Wime_java.repository.TareaCompartidaRepository;
import com.example.Wime_java.repository.TareaRepository;
import com.opencsv.CSVReader;


@Service
public class ImportService {



    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

        @Autowired
    private TareaCompartidaRepository tareaCompartidaRepository;


    private final List<DateTimeFormatter> formatters = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy")
    );

public void importarDatos(String tipo, MultipartFile file, Long idUsuario) {

    try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {

        List<String[]> filas = reader.readAll();

        if (filas.size() <= 1) {
            throw new RuntimeException("El CSV no tiene datos válidos.");
        }

        for (int i = 1; i < filas.size(); i++) {

            String[] fila = filas.get(i);
            if (fila.length < 4) continue;

            String nombre = limpiarValor(fila[0]);
            String prioridad = limpiarValor(fila[1]);
            String fecha = limpiarValor(fila[2]);
            String estado = limpiarValor(fila[3]);
            String descripcion = fila.length >= 5 ? limpiarValor(fila[4]) : "";

            if (nombre.isBlank()) continue;

            if (prioridad.isBlank()) prioridad = "media";

            if (tipo.equalsIgnoreCase("tareas")) {

                Tarea t = new Tarea();
                t.setIdUsuario(idUsuario);
                t.setTitulo(nombre);
                t.setPrioridad(prioridad);
                t.setEstado(estado.isBlank() ? "PENDIENTE" : estado.toUpperCase());
                t.setDescripcion(descripcion.isBlank() ? null : descripcion);

                if (!fecha.isBlank()) {
                    t.setFechaLimite(parseFecha(fecha));
                }

                tareaRepository.save(t);

                if (!tareaCompartidaRepository.existsByIdTareaAndIdUsuario(t.getIdTarea(), idUsuario)) {
                    TareaCompartida relacion = new TareaCompartida();
                    relacion.setIdTarea(t.getIdTarea());
                    relacion.setIdUsuario(idUsuario);
                    relacion.setRol(TareaCompartida.Rol.CREADOR);
                    tareaCompartidaRepository.save(relacion);
                }


            } else if (tipo.equalsIgnoreCase("rutinas")) {

                Rutina r = new Rutina();
                r.setIdUsuario(idUsuario);
                r.setNombreRutina(nombre);
                r.setPrioridad(prioridad);
                r.setEstado(estado.isBlank() ? "pendiente" : estado.toLowerCase());
                r.setFechaAsignacion(LocalDate.now());
                r.setFrecuencia("diaria");
                r.setDescripcion(descripcion.isBlank() ? null : descripcion);

                if (!fecha.isBlank()) {
                    r.setFechaFin(parseFecha(fecha));
                } else {
                    r.setFechaFin(LocalDate.now());
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

    private LocalDate parseFecha(String fecha) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(fecha, formatter);
            } catch (DateTimeParseException e) {}
        }

        throw new IllegalArgumentException("Formato de fecha inválido: " + fecha);
    }

    private String limpiarValor(String valor) {
        return valor == null ? "" : valor.trim();
    }
}