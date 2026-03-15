/* 
package com.example.Wime_java.config;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UsuarioRepository usuarioRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {

            // Usuario ADMIN
            usuarioRepository.findByEmailUsuario("admin@wime.com").ifPresentOrElse(
                    u -> System.out.println("👤 Usuario admin ya existe."),
                    () -> {
                        Usuario admin = new Usuario();
                        admin.setNombreUsuario("Admin");
                        admin.setEmailUsuario("admin@wime.com");
                        admin.setContrasenaUsuario(passwordEncoder.encode("admin123")); // contraseña encriptada
                        admin.setTipo("ADMIN");
                        admin.setEstado("ACTIVO");
                        admin.setFechaRegistro(LocalDateTime.now());
                        admin.setEdad(30); // 👈 valor fijo para evitar error

                        usuarioRepository.save(admin);
                        System.out.println(" Usuario admin creado con éxito.");
                    }
            );

            // Usuario NORMAL
            usuarioRepository.findByEmailUsuario("user@wime.com").ifPresentOrElse(
                    u -> System.out.println("👤 Usuario normal ya existe."),
                    () -> {
                        Usuario user = new Usuario();
                        user.setNombreUsuario("Usuario");
                        user.setEmailUsuario("user@wime.com");
                        user.setContrasenaUsuario(passwordEncoder.encode("user123")); // contraseña encriptada
                        user.setTipo("USER");
                        user.setEstado("ACTIVO");
                        user.setFechaRegistro(LocalDateTime.now());
                        user.setEdad(25); // 👈 valor fijo

                        usuarioRepository.save(user);
                        System.out.println(" Usuario normal creado con éxito.");
                    }
            );
        };
    }
}

*/
