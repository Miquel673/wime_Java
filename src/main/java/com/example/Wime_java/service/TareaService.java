package com.example.Wime_java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.repository.TareaRepository;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public List<Tarea> obtenerTareasPorUsuario(Long idUsuario) {
        return tareaRepository.findByIdUsuario(idUsuario);
    }

    public Optional<Tarea> obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id);
    }

    public Tarea guardarTarea(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    public void eliminarTarea(Long id) {
        tareaRepository.deleteById(id);
    }

    public Tarea actualizarEstado(Long id, String nuevoEstado) {
    Optional<Tarea> tareaOpt = tareaRepository.findById(id);

    if (tareaOpt.isEmpty()) {
        throw new IllegalArgumentException("⚠️ No se encontró la tarea con ID: " + id);
    }

    Tarea tarea = tareaOpt.get();
    tarea.setEstado(nuevoEstado);

    try {
        return tareaRepository.save(tarea);
    } catch (Exception e) {
        throw new RuntimeException("❌ Error al actualizar el estado en la base de datos: " + e.getMessage());
    }
}

}
