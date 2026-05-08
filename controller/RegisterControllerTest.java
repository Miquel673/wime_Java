package com.example.Wime_java.controller;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;

class RegisterControllerTest {

    @InjectMocks
    private RegisterController controller;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistroExitoso() {

        Usuario usuario = new Usuario();
        usuario.setEmailUsuario("test@mail.com");
        usuario.setNombreUsuario("Miguel");
        usuario.setContrasenaUsuario("1234");

        when(usuarioRepository.existsByEmailUsuario("test@mail.com"))
                .thenReturn(false);

        ResponseEntity<Map<String, Object>> response =
                controller.registrarUsuario(usuario);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue((Boolean) response.getBody().get("success"));

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testRegistroDatosIncompletos() {

        Usuario usuario = new Usuario();
        usuario.setEmailUsuario(null); // dato faltante

        ResponseEntity<Map<String, Object>> response =
                controller.registrarUsuario(usuario);

        assertEquals(400, response.getStatusCodeValue());
        assertFalse((Boolean) response.getBody().get("success"));
    }

    @Test
    void testRegistroCorreoExistente() {

        Usuario usuario = new Usuario();
        usuario.setEmailUsuario("test@mail.com");
        usuario.setNombreUsuario("Miguel");
        usuario.setContrasenaUsuario("1234");

        when(usuarioRepository.existsByEmailUsuario("test@mail.com"))
                .thenReturn(true);

        ResponseEntity<Map<String, Object>> response =
                controller.registrarUsuario(usuario);

        assertEquals(400, response.getStatusCodeValue());
        assertFalse((Boolean) response.getBody().get("success"));
    }

    @Test
    void testRegistroErrorInterno() {

        Usuario usuario = new Usuario();
        usuario.setEmailUsuario("test@mail.com");
        usuario.setNombreUsuario("Miguel");
        usuario.setContrasenaUsuario("1234");

        when(usuarioRepository.existsByEmailUsuario(anyString()))
                .thenThrow(new RuntimeException("Error DB"));

        ResponseEntity<Map<String, Object>> response =
                controller.registrarUsuario(usuario);

        assertEquals(500, response.getStatusCodeValue());
        assertFalse((Boolean) response.getBody().get("success"));
    }
}