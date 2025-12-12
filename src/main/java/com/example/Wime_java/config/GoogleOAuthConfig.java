package com.example.Wime_java.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthConfig {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;  // 🔥 Este te faltaba

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.scopes}")
    private String scopes;

    // 🔥 Construye la URL para iniciar el login
    public String buildAuthUrl() {

        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=" + scopes
                + "&access_type=offline"
                + "&prompt=consent";
    }

    // ---- GETTERS ----
    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {   // 🔥 Necesitado por GoogleCalendarService
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getScopes() {
        return scopes;
    }
}
