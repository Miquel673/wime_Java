// src/main/java/com/wime/config/SecurityConfig.java
package com.wime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para pruebas con Postman
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/login").permitAll() // Permitir login sin autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
