package com.example.Wime_java.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.Wime_java.config.TareaConfig;
import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.repository.TareaCompartidaRepository;
import com.example.Wime_java.repository.TareaRepository;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final TareaCompartidaRepository tareaCompartidaRepository;
    private final TareaConfig tareaConfig;

    public TareaService(
            TareaRepository tareaRepository,
            TareaCompartidaRepository tareaCompartidaRepository,
            TareaConfig tareaConfig) {

        this.tareaRepository = tareaRepository;
        this.tareaCompartidaRepository = tareaCompartidaRepository;
        this.tareaConfig = tareaConfig;
    }

    // ==========================================================
    // 📌 LISTAR TAREAS DEL USUARIO
    // (propias + compartidas)
    // ==========================================================
    public List<Tarea> obtenerTareasPorUsuario(Long idUsuario) {

        // 1️⃣ Tareas creadas por el usuario
        List<Tarea> tareasPropias =
                tareaRepository.findByIdUsuario(idUsuario);

        // 2️⃣ IDs de tareas compartidas con el usuario
        List<Long> idsCompartidas =
                tareaCompartidaRepository.findIdsTareasCompartidas(idUsuario);

        // 3️⃣ Obtener las tareas compartidas reales
        List<Tarea> tareasCompartidas = idsCompartidas.isEmpty()
                ? List.of()
                : tareaRepository.findByIdTareaIn(idsCompartidas);

        // 4️⃣ Unificar ambas listas sin duplicados
        return Stream
                .concat(tareasPropias.stream(), tareasCompartidas.stream())
                .collect(Collectors.toMap(
                        Tarea::getIdTarea,
                        t -> t,
                        (a, b) -> a
                ))
                .values()
                .stream()
                .toList();
    }

    // ==========================================================
    // 📌 OBTENER TAREA POR ID
    // ==========================================================
    public Optional<Tarea> obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id);
    }

    // ==========================================================
    // 📌 GUARDAR TAREA
    // ==========================================================
    public Tarea guardarTarea(Tarea tarea) {

        if (tarea.getEstado() == null) {
            tarea.setEstado(tareaConfig.getEstadoPorDefecto());
        }

        if (!tareaConfig.estadoValido(tarea.getEstado())) {
            throw new IllegalArgumentException(
                    "⚠️ Estado de tarea inválido");
        }

        return tareaRepository.save(tarea);
    }

    // ==========================================================
    // 📌 ELIMINAR TAREA
    // ==========================================================
    public void eliminarTarea(Long id) {
        tareaRepository.deleteById(id);
    }

    // ==========================================================
    // 📌 ACTUALIZAR ESTADO DE LA TAREA
    // ==========================================================
    public Tarea actualizarEstado(Long id, String nuevoEstado) {

        String estadoNormalizado = nuevoEstado
                .trim()
                .toUpperCase()
                .replace(" ", "_");

        if (!tareaConfig.estadoValido(estadoNormalizado)) {
            throw new IllegalArgumentException(
                    "⚠️ Estado inválido: " + nuevoEstado);
        }

        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "⚠️ No se encontró la tarea con ID: " + id));

        tarea.setEstado(estadoNormalizado);
        return tareaRepository.save(tarea);
    }

    public void actualizarTareasVencidas(Long idUsuario) {

    LocalDate hoy = LocalDate.now();

    List<Tarea> tareas = tareaRepository.findByIdUsuario(idUsuario);

    for (Tarea tarea : tareas) {

        if (tarea.getFechaLimite() == null) continue;

        boolean estaVencida =
                tarea.getFechaLimite().isBefore(hoy);

        boolean noEsCompletada =
                !"completada".equalsIgnoreCase(tarea.getEstado());

        boolean noEsYaVencida =
                !"vencida".equalsIgnoreCase(tarea.getEstado());

        if (estaVencida && noEsCompletada && noEsYaVencida) {

            tarea.setEstado("vencida");
            tareaRepository.save(tarea);
        }
    }
}
}
