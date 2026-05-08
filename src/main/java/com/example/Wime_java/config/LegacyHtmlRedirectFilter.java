package com.example.Wime_java.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LegacyHtmlRedirectFilter extends OncePerRequestFilter {

    private static final Map<String, String> LEGACY_TO_FRIENDLY = Map.of(
            "/index.html", "/inicio",
            "/HTML/Interfaces/Wime_interfaz_Tablero.html", "/tablero",
            "/HTML/Interfaces/Wime_interfaz_Cuenta.html", "/cuenta",
            "/HTML/Interfaces/Wime_interfaz_BandejaEntrada.html", "/notificaciones"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getDispatcherType() != DispatcherType.REQUEST;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String redirectTo = LEGACY_TO_FRIENDLY.get(uri);

        if (redirectTo != null) {
            String query = request.getQueryString();
            String target = (query == null || query.isBlank()) ? redirectTo : redirectTo + "?" + query;
            response.sendRedirect(target);
            return;
        }

        filterChain.doFilter(request, response);
    }
}