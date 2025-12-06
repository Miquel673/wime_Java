package com.example.Wime_java.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Carpeta absoluta en disco donde se guardan las fotos
    private static final String UPLOAD_DIR_FILESYSTEM = System.getProperty("user.dir") + "/uploads/fotos_perfil/";
    // Ruta relativa guardada en BD para ser servida por el frontend
    private static final String UPLOAD_DIR_WEB = "/uploads/fotos_perfil/";

    public Usuario registrarUsuario(Usuario usuario) {
    usuario.setEstado("Activo");
    usuario.setTipo("Usuario");
    usuario.setUltimoLogin(null);

    return usuarioRepository.save(usuario);
}


    // 🔹 LOGIN DE USUARIO (tu implementación original)
    public Optional<Usuario> login(String email, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailUsuario(email);

        if (usuarioOpt.isEmpty()) {
            System.out.println("❌ Usuario no encontrado con email: " + email);
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();

        boolean passwordOk = BCrypt.checkpw(contrasena, usuario.getContrasenaUsuario());
        if (!passwordOk) {
            System.out.println("❌ Contraseña incorrecta para usuario: " + email);
            return Optional.empty();
        }

        if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
            System.out.println("⚠️ Usuario no está activo: " + email);
            return Optional.empty();
        }

        if (usuario.getUltimoLogin() != null) {
            LocalDateTime limite = usuario.getUltimoLogin().plusDays(60);
            if (LocalDateTime.now().isAfter(limite)) {
                usuario.setEstado("Inactivo");
                usuarioRepository.save(usuario);
                System.out.println("⚠️ Usuario inactivo por más de 60 días: " + email);
                return Optional.empty();
            }
        }

        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return Optional.of(usuario);
    }

    public String obtenerRutaFoto(int idUsuario) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
    if (usuarioOpt.isPresent()) {
        String ruta = usuarioOpt.get().getFotoPerfil();
        System.out.println("📸 Ruta encontrada en BD: " + ruta);
        return ruta;
    }
    System.out.println("⚠️ Usuario no encontrado con ID: " + idUsuario);
    return null;
}



    // 🔹 OBTENER USUARIO POR ID
    public Usuario obtenerPorId(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario).orElse(null);
    }

    // 🔹 ACTUALIZAR FOTO DE PERFIL (solo actualiza la URL en BD)
    public void actualizarFoto(Integer idUsuario, String urlFoto) {
        Usuario usuario = obtenerPorId(idUsuario);
        if (usuario != null) {
            usuario.setFotoPerfil(urlFoto);
            usuarioRepository.save(usuario);
        }
    }

    // 🔹 SUBIR Y GUARDAR FOTO DE PERFIL
public Usuario guardarFoto(Integer idUsuario, MultipartFile file) throws IOException {
    // 📂 Ruta base donde se guardan las fotos
    String UPLOAD_DIR_FILESYSTEM = System.getProperty("user.dir") + "/uploads/fotos_perfil/";

    // 🔍 Verificar existencia del usuario
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
    if (usuarioOpt.isEmpty()) {
        System.out.println("❌ Usuario no encontrado con ID: " + idUsuario);
        return null;
    }

    Usuario usuario = usuarioOpt.get();

    // 🧾 Validar archivo
    if (file == null || file.isEmpty()) {
        System.out.println("⚠️ Archivo vacío o no recibido correctamente.");
        return null;
    }

    // 🧠 Generar nombre único para evitar colisiones
    String nombreArchivo = "usuario_" + idUsuario + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
    Path rutaArchivo = Paths.get(UPLOAD_DIR_FILESYSTEM + nombreArchivo);

    // 🖼️ Guardar archivo físicamente
    Files.write(rutaArchivo, file.getBytes());

    // 🧹 Eliminar foto anterior si existe
    if (usuario.getFotoPerfil() != null && usuario.getFotoPerfil().contains("/uploads/fotos_perfil/")) {
        try {
            String nombreViejo = usuario.getFotoPerfil().substring(usuario.getFotoPerfil().lastIndexOf("/") + 1);
            Path rutaVieja = Paths.get(UPLOAD_DIR_FILESYSTEM + nombreViejo);
            Files.deleteIfExists(rutaVieja);
            System.out.println("🗑️ Foto anterior eliminada: " + nombreViejo);
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo eliminar la foto anterior: " + e.getMessage());
        }
    }

    // 🌐 Guardar ruta accesible desde el frontend
    String urlAccesible = "http://localhost:8080/uploads/fotos_perfil/" + nombreArchivo;
    usuario.setFotoPerfil(urlAccesible);

    // 💾 Actualizar usuario en la BD
    usuarioRepository.save(usuario);
    System.out.println("✅ Foto actualizada correctamente para el usuario: " + idUsuario);

    return usuario;
}

    // 🔹 ELIMINAR FOTO DE PERFIL (archivo + BD)
    public boolean eliminarFoto(Integer idUsuario) {
        Usuario usuario = obtenerPorId(idUsuario);
        if (usuario == null) return false;

        String urlFoto = usuario.getFotoPerfil();
        if (urlFoto != null && urlFoto.contains("/uploads/fotos_perfil/")) {
            String nombreArchivo = urlFoto.substring(urlFoto.lastIndexOf("/") + 1);
            Path rutaArchivo = Paths.get(UPLOAD_DIR_FILESYSTEM + nombreArchivo);
            try {
                Files.deleteIfExists(rutaArchivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        usuario.setFotoPerfil(null);
        usuarioRepository.save(usuario);
        return true;
    }
}
