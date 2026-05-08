package com.example.Wime_java.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.Wime_java.config.GoogleOAuthConfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class GoogleOAuthController {

    private final GoogleOAuthConfig config;

    public GoogleOAuthController(GoogleOAuthConfig config) {
        this.config = config;
    }

    @GetMapping("/auth/google/login")
    public RedirectView loginGoogle(HttpServletRequest request, HttpSession session) {
try {
            String state = UUID.randomUUID().toString();
            session.setAttribute("google_oauth_state", state);

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

            String redirectUri = config.resolveRedirectUri(baseUrl);
            return new RedirectView(config.buildAuthUrl(redirectUri, state));
        } catch (IllegalStateException ex) {
            return new RedirectView("/?googleAuth=google_config_error");
        }
    }
}