package com.example.Wime_java.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
public class ReporteIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void generarReporteTareasConSesion() throws Exception {
        mockMvc.perform(get("/reportes/tareas")
                        .sessionAttr("id_usuario", 1L))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Disposition"));
    }

    @Test
    void generarReporteRutinasConSesion() throws Exception {
        mockMvc.perform(get("/reportes/rutinas")
                        .sessionAttr("id_usuario", 1L))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Disposition"));
    }

    @Test
    void generarReporteTareasSinSesion() throws Exception {
        mockMvc.perform(get("/reportes/tareas"))
                .andExpect(status().isBadRequest());
    }
}