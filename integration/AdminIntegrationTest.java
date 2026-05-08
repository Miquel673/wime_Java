package com.example.Wime_java.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class AdminIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // 🔹 Listar usuarios
    @Test
    void listarUsuarios() throws Exception {
        mockMvc.perform(get("/admin/usuarios"))
                .andExpect(status().isOk());
    }

    // 🔹 Listar notificaciones
    @Test
    void listarNotificaciones() throws Exception {
        mockMvc.perform(get("/admin/notificaciones"))
                .andExpect(status().isOk());
    }

    // 🔹 Eliminar una notificación
    @Test
    void eliminarNotificacion() throws Exception {
        mockMvc.perform(delete("/admin/notificaciones/1"))
                .andExpect(status().isNoContent());
    }

    // 🔹 Eliminar todas las notificaciones
    @Test
    void eliminarTodasNotificaciones() throws Exception {
        mockMvc.perform(delete("/admin/notificaciones"))
                .andExpect(status().isNoContent());
    }

    // 🔹 Cambiar estado de usuario
    @Test
    void cambiarEstadoUsuario() throws Exception {
        mockMvc.perform(put("/admin/estado/1")
                        .param("estado", "activo"))
                .andExpect(status().isOk());
    }

    // 🔹 Cambiar tipo de usuario
    @Test
    void cambiarTipoUsuario() throws Exception {
        mockMvc.perform(put("/admin/tipo/1")
                        .param("tipo", "admin"))
                .andExpect(status().isOk());
    }

    // 🔹 Eliminar usuario
    @Test
    void eliminarUsuario() throws Exception {
        mockMvc.perform(delete("/admin/eliminar/1"))
                .andExpect(status().isOk());
    }

    // 🔹 Generar reporte PDF (con sesión)
    @Test
    void generarReporteAdminConSesion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id_usuario", 1L);

        mockMvc.perform(get("/admin/reporte/pdf")
                        .session(session))
                .andExpect(status().isOk());
    }

    // 🔹 Generar reporte PDF (sin sesión)
    @Test
    void generarReporteAdminSinSesion() throws Exception {
        mockMvc.perform(get("/admin/reporte/pdf"))
                .andExpect(status().isUnauthorized());
    }
}