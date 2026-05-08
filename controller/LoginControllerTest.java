package com.example.Wime_java.controller;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

class LoginControllerTest {

    private LoginController controller;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new LoginController(usuarioService);
    }

    // ---------------------------------------------------------
    // 🔹 LOGIN EXITOSO
    // ---------------------------------------------------------
    @Test
    void testLoginExitoso() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombreUsuario("Miguel");
        usuario.setTipo("usuario");

        when(usuarioService.login("test@mail.com", "1234"))
                .thenReturn(Optional.of(usuario));

        Map<String, Object> response = controller.login(
                "test@mail.com",
                "1234",
                session
        );

        assertTrue((Boolean) response.get("success"));
        assertEquals("Miguel", response.get("usuario"));

        verify(session).setAttribute("usuario", usuario);
        verify(session).setAttribute("id_usuario", usuario.getIdUsuario().longValue());
        verify(session).setAttribute("rol", usuario.getTipo());
        verify(session).setAttribute("wime_session_active", true);
    }

    // ---------------------------------------------------------
    // 🔹 LOGIN FALLIDO
    // ---------------------------------------------------------
    @Test
    void testLoginFallido() {

        when(usuarioService.login("test@mail.com", "wrong"))
                .thenReturn(Optional.empty());

        Map<String, Object> response = controller.login(
                "test@mail.com",
                "wrong",
                session
        );

        assertFalse((Boolean) response.get("success"));
        assertEquals(" Usuario o contraseña incorrectos o cuenta inactiva.",
                response.get("message"));
    }

    // ---------------------------------------------------------
    // 🔹 LOGIN CON ERROR (EXCEPCIÓN)
    // ---------------------------------------------------------
    @Test
    void testLoginError() {

        when(usuarioService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("Error BD"));

        Map<String, Object> response = controller.login(
                "test@mail.com",
                "1234",
                session
        );

        assertFalse((Boolean) response.get("success"));
        assertEquals(" Error interno en el servidor.", response.get("message"));
    }

    // ---------------------------------------------------------
    // 🔹 CHECK SESSION ACTIVA
    // ---------------------------------------------------------
    @Test
    void testCheckSessionActiva() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombreUsuario("Miguel");
        usuario.setTipo("admin");

        when(session.getAttribute("usuario")).thenReturn(usuario);

        Map<String, Object> response = controller.checkSession(session);

        assertTrue((Boolean) response.get("active"));
        assertEquals("Miguel", response.get("usuario"));
        assertEquals("admin", response.get("rol"));
    }

    // ---------------------------------------------------------
    // 🔹 CHECK SESSION INACTIVA
    // ---------------------------------------------------------
    @Test
    void testCheckSessionInactiva() {

        when(session.getAttribute("usuario")).thenReturn(null);

        Map<String, Object> response = controller.checkSession(session);

        assertFalse((Boolean) response.get("active"));
    }

    // ---------------------------------------------------------
    // 🔹 LOGOUT EXITOSO
    // ---------------------------------------------------------
    @Test
    void testLogoutExitoso() {

        Map<String, Object> response = controller.logout(session);

        assertTrue((Boolean) response.get("success"));
        verify(session).invalidate();
    }

    // ---------------------------------------------------------
    // 🔹 LOGOUT CON ERROR
    // ---------------------------------------------------------
    @Test
    void testLogoutError() {

        doThrow(new RuntimeException("Error sesión"))
                .when(session).invalidate();

        Map<String, Object> response = controller.logout(session);

        assertFalse((Boolean) response.get("success"));
    }
}