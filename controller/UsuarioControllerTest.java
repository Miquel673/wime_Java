package com.example.Wime_java.controller;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.service.EmailService;
import com.example.Wime_java.service.PasswordRecoveryService;
import com.example.Wime_java.service.UsuarioService;

class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController controller;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordRecoveryService passwordRecoveryService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ==============================
    // 🔹 ACTUALIZAR NOMBRE
    // ==============================

    @Test
    void testActualizarNombreExitoso() {

        Integer id = 1;

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id.longValue()); // 👈 aquí está la clave
        usuario.setNombreUsuario("Viejo");

        when(usuarioService.obtenerPorId(id)).thenReturn(usuario);

        Map<String, String> body = Map.of("nombre", "NuevoNombre");

        ResponseEntity<?> response = controller.actualizarNombre(id, body);

        assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    void testActualizarNombreVacio() {

        Map<String, String> body = Map.of("nombre", "");

        ResponseEntity<?> response = controller.actualizarNombre(1, body);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testActualizarNombreUsuarioNoExiste() {

        when(usuarioService.obtenerPorId(1)).thenReturn(null);

        Map<String, String> body = Map.of("nombre", "Miguel");

        ResponseEntity<?> response = controller.actualizarNombre(1, body);

        assertEquals(400, response.getStatusCodeValue());
    }

    // ==============================
    // 🔹 REGISTRAR USUARIO
    // ==============================

    @Test
    void testRegistrarUsuarioExitoso() {

        Map<String, String> datos = Map.of(
            "EmailUsuario", "test@mail.com",
            "NombreUsuario", "Miguel",
            "ContrasenaUsuario", "Password1!",
            "Birth_Day", "2000-01-01"
        );

        Usuario usuarioMock = new Usuario();

        when(usuarioService.registrarUsuario(any())).thenReturn(usuarioMock);

        ResponseEntity<?> response = controller.registrarUsuario(datos);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRegistrarUsuarioDatosFaltantes() {

        Map<String, String> datos = Map.of(
            "EmailUsuario", "test@mail.com"
        );

        ResponseEntity<?> response = controller.registrarUsuario(datos);

        assertEquals(400, response.getStatusCodeValue());
    }

    // ==============================
    // 🔹 ELIMINAR CUENTA
    // ==============================

    @Test
    void testEliminarCuentaExitoso() {

        Integer id = 1;

        Usuario usuario = new Usuario();
        usuario.setContrasenaUsuario("hashedPassword");

        when(usuarioService.obtenerPorId(id)).thenReturn(usuario);
        when(passwordEncoder.matches("1234", "hashedPassword")).thenReturn(true);
        when(usuarioService.eliminarCuenta(id)).thenReturn(true);

        Map<String, String> body = Map.of("password", "1234");

        ResponseEntity<?> response = controller.eliminarCuenta(id, body);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testEliminarCuentaPasswordIncorrecta() {

        Integer id = 1;

        Usuario usuario = new Usuario();
        usuario.setContrasenaUsuario("hashedPassword");

        when(usuarioService.obtenerPorId(id)).thenReturn(usuario);
        when(passwordEncoder.matches("wrong", "hashedPassword")).thenReturn(false);

        Map<String, String> body = Map.of("password", "wrong");

        ResponseEntity<?> response = controller.eliminarCuenta(id, body);

        assertEquals(400, response.getStatusCodeValue());
    }

    // ==============================
    // 🔹 CAMBIAR PASSWORD
    // ==============================

    @Test
    void testCambiarPasswordExitoso() {

        Integer id = 1;

        Usuario usuario = new Usuario();
        usuario.setContrasenaUsuario("hashedPassword");

        when(usuarioService.obtenerPorId(id)).thenReturn(usuario);
        when(passwordEncoder.matches("1234", "hashedPassword")).thenReturn(true);

        Map<String, String> body = Map.of(
            "currentPassword", "1234",
            "password", "Nueva123!"
        );

        ResponseEntity<?> response = controller.cambiarPassword(id, body);

        assertEquals(200, response.getStatusCodeValue());
    }
}