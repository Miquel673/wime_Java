package com.example.Wime_java.config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleOAuthConfig {

    @Value("${google.client.id:}")
    private String clientId;

    @Value("${google.client.secret:}")
    private String clientSecret;

    @Value("${google.redirect.uri:}")
    private String redirectUri;

    @Value("${google.scopes:openid email profile}")
    private String scopes;

    public String buildAuthUrl(String resolvedRedirectUri, String state) {
        validateCredentials();

        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("client_id", clientId);
        queryParams.put("redirect_uri", resolvedRedirectUri);
        queryParams.put("response_type", "code");
        queryParams.put("scope", String.join(" ", getScopeList()));
        queryParams.put("access_type", "offline");
        queryParams.put("prompt", "consent");
        queryParams.put("include_granted_scopes", "true");

        if (StringUtils.hasText(state)) {
            queryParams.put("state", state);
        }

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth");

        queryParams.forEach(builder::queryParam);
        return builder.encode(StandardCharsets.UTF_8).toUriString();
    }

    public String resolveRedirectUri(String requestBaseUrl) {
        if (!StringUtils.hasText(redirectUri)) {
            throw new IllegalStateException("La propiedad google.redirect.uri no está configurada.");
        }

        if (redirectUri.startsWith("http://") || redirectUri.startsWith("https://")) {
            return redirectUri;
        }

        if (!StringUtils.hasText(requestBaseUrl)) {
            throw new IllegalStateException("No se pudo resolver la URL base para Google OAuth.");
        }

        String normalizedBaseUrl = requestBaseUrl.endsWith("/")
                ? requestBaseUrl.substring(0, requestBaseUrl.length() - 1)
                : requestBaseUrl;

        String normalizedRedirectUri = redirectUri.startsWith("/") ? redirectUri : "/" + redirectUri;
        return normalizedBaseUrl + normalizedRedirectUri;
    }

    public List<String> getScopeList() {
        return Arrays.stream(scopes.split("\\s+"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    public void validateCredentials() {
        if (!StringUtils.hasText(clientId) || !StringUtils.hasText(clientSecret)) {
            throw new IllegalStateException(
                    "Faltan las credenciales de Google OAuth. Define google.client.id y google.client.secret.");
        }
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getScopes() {
        return scopes;
    }
}