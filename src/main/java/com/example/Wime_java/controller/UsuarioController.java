package com.example.Wime_java.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Usaremos rutas absolutas solo para operaciones en disco; el frontend consumirá la URL relativa guardada en la BD.
    private static final String UPLOAD_DIR_FILESYSTEM = System.getProperty("user.dir") + "/uploads/fotos_perfil/";
    private static final String UPLOAD_DIR_WEB = "/uploads/fotos_perfil/"; // ruta relativa para servir desde el frontend

    // 🔹 SUBIR O ACTUALIZAR FOTO DE PERFIL
@PostMapping("/subir-foto")
public ResponseEntity<?> subirFoto(
        @RequestParam("file") MultipartFile file,
        @RequestParam("idUsuario") Integer idUsuario) {

    // 🟢 Carpeta donde se guardarán las fotos
    String UPLOAD_DIR_FILESYSTEM = System.getProperty("user.dir") + "/uploads/fotos_perfil/";

    try {
        // 1️⃣ Crear carpeta si no existe
        Path directorio = Paths.get(UPLOAD_DIR_FILESYSTEM);
        if (!Files.exists(directorio)) {
            Files.createDirectories(directorio);
            System.out.println("📁 Carpeta creada: " + directorio.toAbsolutePath());
        }

        // 2️⃣ Verificar archivo válido
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "message", "El archivo está vacío o no se envió correctamente.")
            );
        }

        // 3️⃣ Llamar al servicio para guardar la foto
        Usuario usuario = usuarioService.guardarFoto(idUsuario, file);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Usuario no encontrado"));
        }

        // 4️⃣ Retornar la URL pública de la foto
        String url = usuario.getFotoPerfil() != null ? usuario.getFotoPerfil() : "";
        return ResponseEntity.ok(Map.of(
                "success", true,
                "urlFoto", url,
                "message", "Foto actualizada correctamente."
        ));

    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error de E/S al guardar la foto: " + e.getMessage()));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error inesperado: " + e.getMessage()));
    }
}

@GetMapping("/usuarios/{idUsuario}/foto")
public ResponseEntity<Map<String, String>> obtenerFotoPerfil(@PathVariable int idUsuario) {
    try {
        String rutaFoto = usuarioService.obtenerRutaFoto(idUsuario);

        Map<String, String> response = new HashMap<>();
        response.put("fotoPerfil", 
            (rutaFoto != null && !rutaFoto.isEmpty())
                ? rutaFoto
                : "/IMG/vector-de-perfil-avatar-predeterminado-foto-usuario-medios-sociales-icono-183042379.jpeg"
        );

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        e.printStackTrace();
        Map<String, String> response = new HashMap<>();
        response.put("fotoPerfil", "/IMG/vector-de-perfil-avatar-predeterminado-foto-usuario-medios-sociales-icono-183042379.jpeg");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


    // 🔹 OBTENER URL DE LA FOTO DEL USUARIO (devuelve JSON con urlFoto)
    @GetMapping("/{idUsuario}/foto")
    public ResponseEntity<Map<String, String>> obtenerFotoPerfil(@PathVariable Integer idUsuario) {
        try {
            String rutaFoto = usuarioService.obtenerRutaFoto(idUsuario);

            System.out.println("🧩 Obteniendo foto para usuario ID " + idUsuario + ": " + rutaFoto);

            Map<String, String> response = new HashMap<>();
            response.put("fotoPerfil",
                (rutaFoto != null && !rutaFoto.isEmpty())
                    ? rutaFoto
                    : "/IMG/vector-de-perfil-avatar-predeterminado-foto-usuario-medios-sociales-icono-183042379.jpeg"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("fotoPerfil", "/IMG/vector-de-perfil-avatar-predeterminado-foto-usuario-medios-sociales-icono-183042379.jpeg");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 🔹 ELIMINAR FOTO DE PERFIL (RESTABLECER AVATAR POR DEFECTO)
    @DeleteMapping("/{idUsuario}/eliminar-foto")
    public ResponseEntity<?> eliminarFoto(@PathVariable Integer idUsuario) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(idUsuario);
            if (usuario == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Usuario no encontrado"));
            }

            boolean eliminado = usuarioService.eliminarFoto(idUsuario);
            if (!eliminado) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("success", false, "message", "No se pudo eliminar la foto"));
            }

            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al eliminar la foto: " + e.getMessage()));
        }
    }
}
