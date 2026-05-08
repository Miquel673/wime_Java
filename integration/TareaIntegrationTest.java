package com.example.Wime_java.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;

@AutoConfigureMockMvc(addFilters = false)
public class TareaIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();

        Usuario user = new Usuario();
        user.setNombreUsuario("Test");
        user.setEmailUsuario("test@test.com");
        user.setContrasenaUsuario("1234");
        user.setTipo("usuario");
        user.setEstado("activo");

        usuarioRepository.save(user);
    }

    @Test
    void crearTarea() throws Exception {
        mockMvc.perform(post("/api/tareas/crear")
                        .param("idUsuario", "1")
                        .param("titulo", "Tarea test")
                        .param("descripcion", "Descripción test")
                        .param("prioridad", "ALTA"))
                .andExpect(status().isOk());
    }

    @Test
    void listarTareas() throws Exception {
        mockMvc.perform(get("/api/tareas/1"))
                .andExpect(status().isOk());
    }
}