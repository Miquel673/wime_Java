package com.example.Wime_java.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.example.Wime_java.Auth.OAuthSessionStore;
import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class GoogleOAuthCallbackController {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final OAuthSessionStore sessionStore;
    private final UsuarioRepository usuarioRepository;

    public GoogleOAuthCallbackController(OAuthSessionStore sessionStore, UsuarioRepository usuarioRepository) {
        this.sessionStore = sessionStore;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/auth/google/callback")
    public RedirectView callback(String code, HttpSession session) throws IOException {

        // 1. Intercambiar CODE por ACCESS TOKEN
        RestTemplate rest = new RestTemplate();
        String tokenUrl = "https://oauth2.googleapis.com/token";

        Map<String, String> requestBody = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "code", code,
                "grant_type", "authorization_code",
                "redirect_uri", redirectUri
        );

        ResponseEntity<Map> tokenResponse = rest.postForEntity(tokenUrl, requestBody, Map.class);
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 2. Obtener información del usuario desde Google
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = rest.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map userInfo = userInfoResponse.getBody();

        // DATOS TRAÍDOS DESDE GOOGLE
        String emailGoogle = (String) userInfo.get("email");
        String nombreGoogle = (String) userInfo.get("name");

        // ---------------------------------------------
        // 3. Registrar o actualizar el usuario en la BD
        // ---------------------------------------------

        Usuario usuario = usuarioRepository.findByEmailUsuario(emailGoogle).orElse(null);

        if (usuario == null) {
            // Registrar nuevo usuario proveniente de Google
            usuario = new Usuario();
            usuario.setEmailUsuario(emailGoogle);
            usuario.setNombreUsuario(nombreGoogle);
            //usuario.setBirthDay(0); // o 18 o lo que quiera
            usuario.setContrasenaUsuario("GOOGLE_USER"); // Contraseña dummy
            usuario.setEstado("Activo");
            usuario.setTipo("Corriente");
            usuario.setUltimoLogin(LocalDateTime.now());
            usuarioRepository.save(usuario);
        } else {
            // Usuario ya existía → solo actualizar el último login
            usuario.setUltimoLogin(LocalDateTime.now());
            usuarioRepository.save(usuario);
        }

            // 4. Guardar en sesión el usuario completo (COHERENTE CON LOGIN LOCAL)
            session.setAttribute("usuario", usuario);
            session.setAttribute("id_usuario", usuario.getIdUsuario().longValue());
            session.setAttribute("rol", usuario.getTipo());
            session.setAttribute("wime_session_active", true);


        // 5. Redirigir al tablero
        return new RedirectView("../../HTML/Interfaces/Wime_interfaz_Tablero.html");
    }
}
