package com.example.Wime_java.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;



@AutoConfigureMockMvc(addFilters = false)
public class LoginIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();

        Usuario user = new Usuario();
        user.setNombreUsuario("Miguel");
        user.setEmailUsuario("test@test.com");
        user.setContrasenaUsuario(encoder.encode("1234"));
        user.setTipo("usuario");
        user.setEstado("activo");

        usuarioRepository.save(user);
    }

    @Test
    void loginExitoso() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("email", "test@test.com")
                        .param("contrasena", "1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void loginFallido() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("email", "test@test.com")
                        .param("contrasena", "incorrecta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }
}