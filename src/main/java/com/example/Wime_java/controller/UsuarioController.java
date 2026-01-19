package com.example.Wime_java.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Wime_java.model.Usuario;
import com.example.Wime_java.service.EmailService;
import com.example.Wime_java.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")

public class UsuarioController {


    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;


    private static final String UPLOAD_DIR_FILESYSTEM = System.getProperty("user.dir") + "/uploads/fotos_perfil/";
    private static final String UPLOAD_DIR_WEB = "/uploads/fotos_perfil/";

    // ---------------------------------------------------------
    // 🔹 REGISTRO DE USUARIO (Nuevo método)
    // ---------------------------------------------------------

    // 🔹 ACTUALIZAR NOMBRE DE USUARIO
@PostMapping("/{idUsuario}/actualizar-nombre")
public ResponseEntity<?> actualizarNombre(
        @PathVariable Integer idUsuario,
        @RequestBody Map<String, String> body) {

    try {
        String nuevoNombre = body.get("nombre");

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "El nombre no puede estar vacío"));
        }

        Usuario usuario = usuarioService.obtenerPorId(idUsuario);
        if (usuario == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Usuario no encontrado"));
        }

        usuario.setNombreUsuario(nuevoNombre);
        usuarioService.actualizarUsuario(usuario);


        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Nombre actualizado correctamente",
                "nombre", nuevoNombre
        ));

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", e.getMessage()));
    }
}


    
@PostMapping("/registrar")
public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> datos) {
    try {
        String email = datos.get("EmailUsuario");
        String nombre = datos.get("NombreUsuario");
        String contrasena = datos.get("ContrasenaUsuario");
        String birthDayStr = datos.get("Birth_Day");

        if (email == null || nombre == null || contrasena == null || birthDayStr == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Faltan datos obligatorios"));
        }

        LocalDate birthDay = LocalDate.parse(birthDayStr);
        int edad = Period.between(birthDay, LocalDate.now()).getYears();

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmailUsuario(email);
        nuevoUsuario.setNombreUsuario(nombre);
        nuevoUsuario.setContrasenaUsuario(contrasena);
        nuevoUsuario.setTipo("Corriente");
        nuevoUsuario.setEstado("Activo");
        nuevoUsuario.setUltimoLogin(null);
        nuevoUsuario.setBirthDay(edad);

        // Cifrar contraseña
        String contrasenaHash = passwordEncoder.encode(contrasena);
        nuevoUsuario.setContrasenaUsuario(contrasenaHash);

        Usuario creado = usuarioService.registrarUsuario(nuevoUsuario);

        // 🔹 Enviar correo al usuario
        try {
            String asunto = "Registro exitoso en Wime";
            String mensaje = "Hola " + nombre + ", tu correo ha sido registrado exitosamente en Wime.";
            emailService.sendMassEmail(List.of(email), asunto, mensaje);
        } catch (Exception ex) {
            System.out.println("⚠️ No se pudo enviar el correo: " + ex.getMessage());
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Usuario registrado correctamente",
            "usuario", creado
        ));

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", e.getMessage()));
    }
}


    // 🔹 SUBIR O ACTUALIZAR FOTO DE PERFIL
    @PostMapping("/subir-foto")
    public ResponseEntity<?> subirFoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("idUsuario") Integer idUsuario) {

        String UPLOAD_DIR_FILESYSTEM = System.getProperty("user.dir") + "/uploads/fotos_perfil/";

        try {
            Path directorio = Paths.get(UPLOAD_DIR_FILESYSTEM);
            if (!Files.exists(directorio)) {
                Files.createDirectories(directorio);
                System.out.println("📁 Carpeta creada: " + directorio.toAbsolutePath());
            }

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "El archivo está vacío o no se envió correctamente.")
                );
            }

            Usuario usuario = usuarioService.guardarFoto(idUsuario, file);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("success", false, "message", "Usuario no encontrado"));
            }

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

    // 🔹 OBTENER FOTO DEL USUARIO
    @GetMapping("/{idUsuario}/foto")
    public ResponseEntity<Map<String, String>> obtenerFotoPerfil(@PathVariable Integer idUsuario) {
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

    // 🔹 ELIMINAR FOTO
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
