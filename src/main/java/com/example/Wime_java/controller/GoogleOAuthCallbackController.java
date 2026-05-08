package com.example.Wime_java.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.example.Wime_java.Auth.OAuthSessionStore;
import com.example.Wime_java.config.GoogleOAuthConfig;
import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class GoogleOAuthCallbackController {

    private final GoogleOAuthConfig googleOAuthConfig;
    private final OAuthSessionStore sessionStore;
    private final UsuarioRepository usuarioRepository;
    private final RestTemplate restTemplate;

    public GoogleOAuthCallbackController(
            GoogleOAuthConfig googleOAuthConfig,
            OAuthSessionStore sessionStore,
            UsuarioRepository usuarioRepository,
            RestTemplate restTemplate) {
        this.googleOAuthConfig = googleOAuthConfig;
        this.sessionStore = sessionStore;
        this.usuarioRepository = usuarioRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/auth/google/callback")
    public RedirectView callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            HttpServletRequest request,
            HttpSession session) {

        if (StringUtils.hasText(error)) {
            return buildErrorRedirect("google_access_denied");
        }

        if (!StringUtils.hasText(code)) {
            return buildErrorRedirect("google_missing_code");
        }

        String expectedState = (String) session.getAttribute("google_oauth_state");
        session.removeAttribute("google_oauth_state");

        if (!StringUtils.hasText(expectedState) || !expectedState.equals(state)) {
            return buildErrorRedirect("google_invalid_state");
        }

        try {
            String baseUrl = resolveBaseUrl(request);            String resolvedRedirectUri = googleOAuthConfig.resolveRedirectUri(baseUrl);

            HttpHeaders tokenHeaders = new HttpHeaders();
            tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
            tokenRequest.add("client_id", googleOAuthConfig.getClientId());
            tokenRequest.add("client_secret", googleOAuthConfig.getClientSecret());
            tokenRequest.add("code", code);
            tokenRequest.add("grant_type", "authorization_code");
            tokenRequest.add("redirect_uri", resolvedRedirectUri);

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                    "https://oauth2.googleapis.com/token",
                    new HttpEntity<>(tokenRequest, tokenHeaders),
                    Map.class);

            Map<String, Object> tokenBody = tokenResponse.getBody();
            if (tokenBody == null || !StringUtils.hasText((String) tokenBody.get("access_token"))) {
                return buildErrorRedirect("google_token_error");
            }

            String accessToken = (String) tokenBody.get("access_token");
            String refreshToken = (String) tokenBody.get("refresh_token");
            Long expiresIn = tokenBody.get("expires_in") instanceof Number number
                    ? number.longValue()
                    : null;

            HttpHeaders profileHeaders = new HttpHeaders();
            profileHeaders.setBearerAuth(accessToken);
            HttpEntity<Void> profileRequest = new HttpEntity<>(profileHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v2/userinfo",
                    HttpMethod.GET,
                    profileRequest,
                    Map.class);

            Map<String, Object> userInfo = userInfoResponse.getBody();
            if (userInfo == null) {
                return buildErrorRedirect("google_profile_error");
            }

            String emailGoogle = (String) userInfo.get("email");
            String nombreGoogle = (String) userInfo.getOrDefault("name", emailGoogle);
            String fotoGoogle = (String) userInfo.get("picture");

            if (!StringUtils.hasText(emailGoogle)) {
                return buildErrorRedirect("google_email_missing");
            }

            Usuario usuario = usuarioRepository.findByEmailUsuario(emailGoogle)
                    .orElseGet(() -> crearUsuarioDesdeGoogle(emailGoogle, nombreGoogle, fotoGoogle));

            usuario.setNombreUsuario(StringUtils.hasText(nombreGoogle) ? nombreGoogle : usuario.getNombreUsuario());
            usuario.setEstado("Activo");
            usuario.setUltimoLogin(LocalDateTime.now());
            if (StringUtils.hasText(fotoGoogle)) {
                usuario.setFotoPerfil(fotoGoogle);
            }
            usuarioRepository.save(usuario);

            sessionStore.save(session, accessToken, refreshToken, expiresIn);
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuario_google", usuario.getEmailUsuario());
            session.setAttribute("nombre_google", usuario.getNombreUsuario());
            session.setAttribute("id_usuario", usuario.getIdUsuario().longValue());
            session.setAttribute("rol", usuario.getTipo());
            session.setAttribute("wime_session_active", true);

            return new RedirectView("/tablero?googleAuth=success");
        } catch (IllegalStateException ex) {
            return buildErrorRedirect("google_config_error");
        } catch (Exception ex) {
            return buildErrorRedirect("google_callback_error");
        }
    }

    private Usuario crearUsuarioDesdeGoogle(String emailGoogle, String nombreGoogle, String fotoGoogle) {
        Usuario usuario = new Usuario();
        usuario.setEmailUsuario(emailGoogle);
        usuario.setNombreUsuario(StringUtils.hasText(nombreGoogle) ? nombreGoogle : emailGoogle);
        usuario.setContrasenaUsuario("GOOGLE_USER");
        usuario.setEstado("Activo");
        usuario.setTipo("Usuario");
        usuario.setUltimoLogin(LocalDateTime.now());
        usuario.setFotoPerfil(fotoGoogle);
        return usuarioRepository.save(usuario);
    }

    private RedirectView buildErrorRedirect(String errorCode) {
        return new RedirectView("/?googleAuth=" + errorCode);
    }
private String resolveBaseUrl(HttpServletRequest request) {
        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        String forwardedHost = request.getHeader("X-Forwarded-Host");
        String forwardedPort = request.getHeader("X-Forwarded-Port");

        String scheme = StringUtils.hasText(forwardedProto) ? forwardedProto : request.getScheme();
        String host = StringUtils.hasText(forwardedHost) ? forwardedHost : request.getServerName();
        String port = StringUtils.hasText(forwardedPort)
                ? forwardedPort
                : String.valueOf(request.getServerPort());

        String baseUrl = scheme + "://" + host;
        if (!"80".equals(port) && !"443".equals(port) && !host.contains(":")) {
            baseUrl += ":" + port;
        }

        return baseUrl;
    }
}