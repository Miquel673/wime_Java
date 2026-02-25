package com.example.Wime_java.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.Wime_java.config.TareaConfig;
import com.example.Wime_java.dto.TareaTableroDTO;
import com.example.Wime_java.model.Tarea;
import com.example.Wime_java.model.TareaCompartida;
import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.TareaCompartidaRepository;
import com.example.Wime_java.repository.TareaRepository;
import com.example.Wime_java.repository.UsuarioRepository;


@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final TareaCompartidaRepository tareaCompartidaRepository;
    private final TareaConfig tareaConfig;
    private final UsuarioRepository usuarioRepository;

    public TareaService(
        TareaRepository tareaRepository,
        TareaCompartidaRepository tareaCompartidaRepository,
        TareaConfig tareaConfig,
        UsuarioRepository usuarioRepository) {

    this.tareaRepository = tareaRepository;
    this.tareaCompartidaRepository = tareaCompartidaRepository;
    this.tareaConfig = tareaConfig;
    this.usuarioRepository = usuarioRepository;
}

        // ==========================================================
// 📌 LISTAR TAREAS PARA TABLERO (con info creador)
// ==========================================================
public List<TareaTableroDTO> obtenerTareasParaTablero(Long idUsuario) {

    List<TareaCompartida> relaciones =
            tareaCompartidaRepository.findByIdUsuario(idUsuario);

    List<TareaTableroDTO> resultado = new java.util.ArrayList<>();

    for (TareaCompartida relacion : relaciones) {

        Tarea tarea = tareaRepository
                .findById(relacion.getIdTarea())
                .orElse(null);

        if (tarea == null) continue;

        TareaTableroDTO dto = new TareaTableroDTO();

        dto.setIdTarea(tarea.getIdTarea());
        dto.setTitulo(tarea.getTitulo());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setEstado(tarea.getEstado());
        dto.setPrioridad(tarea.getPrioridad());
        dto.setFechaLimite(tarea.getFechaLimite());

        // 🔎 Buscar creador
        TareaCompartida creadorRelacion =
                tareaCompartidaRepository
                        .findByIdTarea(tarea.getIdTarea())
                        .stream()
                        .filter(r -> r.getRol() == TareaCompartida.Rol.CREADOR)
                        .findFirst()
                        .orElse(null);

        if (creadorRelacion != null) {

            Usuario creador = usuarioRepository
                .findById(creadorRelacion.getIdUsuario().intValue())
                .orElse(null);

            if (creador != null) {
                dto.setIdCreador(creador.getIdUsuario());
                dto.setNombreCreador(creador.getNombreUsuario());
                dto.setImagenPerfilCreador(creador.getFotoPerfil());       
             }
        }

        dto.setEsCompartida(
                relacion.getRol() == TareaCompartida.Rol.COMPARTIDA
        );

        resultado.add(dto);
    }

    return resultado;
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

        boolean estaVencida = tarea.getFechaLimite().isBefore(hoy);

        boolean noEsCompletada =
                !"COMPLETADA".equalsIgnoreCase(tarea.getEstado());

        boolean noEsYaVencida =
                !"VENCIDA".equalsIgnoreCase(tarea.getEstado());

        if (estaVencida && noEsCompletada && noEsYaVencida) {

            tarea.setEstado("VENCIDA");
            tareaRepository.save(tarea);
        }
    }
}

}
