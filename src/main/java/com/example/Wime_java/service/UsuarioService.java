package com.example.Wime_java.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> login(String email, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailUsuario(email);

        if (usuarioOpt.isEmpty()) {
            System.out.println("❌ Usuario no encontrado con email: " + email);
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();

        // 🔎 Logs de depuración
        System.out.println("👉 Email recibido: " + email);
        System.out.println("👉 Contraseña ingresada: " + contrasena);
        System.out.println("👉 Hash almacenado en BD: " + usuario.getContrasenaUsuario());

        // 🔑 Validar contraseña (BCrypt como en PHP password_hash)
        boolean passwordOk = BCrypt.checkpw(contrasena, usuario.getContrasenaUsuario());
        System.out.println("🔍 Resultado BCrypt: " + passwordOk);

        if (!passwordOk) {
            System.out.println("❌ Contraseña incorrecta para usuario: " + email);
            return Optional.empty();
        }

        // 🛑 Verificar estado
        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            System.out.println("⚠️ Usuario no está activo: " + email);
            return Optional.empty();
        }

        // ⏳ Verificar último login (60 días de inactividad = inactivo)
        if (usuario.getUltimoLogin() != null) {
            LocalDateTime limite = usuario.getUltimoLogin().plusDays(60);
            if (LocalDateTime.now().isAfter(limite)) {
                usuario.setEstado("Inactivo");
                usuarioRepository.save(usuario);
                System.out.println("⚠️ Usuario inactivo por más de 60 días: " + email);
                return Optional.empty();
            }
        }

        // 🔄 Actualizar último login
        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        System.out.println("✅ Login exitoso para: " + email);
        return Optional.of(usuario);
    }
}
