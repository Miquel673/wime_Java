package com.example.Wime_java.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads/fotos_perfil");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/uploads/fotos_perfil/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/inicio");
        registry.addViewController("/inicio").setViewName("forward:/index.html");
        registry.addViewController("/tablero").setViewName("forward:/HTML/Interfaces/Wime_interfaz_Tablero.html");
        registry.addViewController("/cuenta").setViewName("forward:/HTML/Interfaces/Wime_interfaz_Cuenta.html");
        registry.addViewController("/notificaciones").setViewName("forward:/HTML/Interfaces/Wime_interfaz_BandejaEntrada.html");
        registry.addViewController("/admin/dashboard").setViewName("redirect:/Admin/HTML/Wime_Interfaz_AdminDashBoard.html");

    }
}