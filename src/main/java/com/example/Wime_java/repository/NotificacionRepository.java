package com.example.Wime_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Wime_java.model.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByIdUsuario(Long idUsuario);

    List<Notificacion> findByIdUsuarioOrderByFechaDesc(Long idUsuario);
    
    List<Notificacion> findAllByOrderByFechaDesc();
    void deleteByIdAndIdUsuario(Long id, Long idUsuario);
    void deleteByIdUsuario(Long idUsuario);
    boolean existsByIdUsuarioAndTipoAndMensaje(Long idUsuario, String tipo, String mensaje);
}
