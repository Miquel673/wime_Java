package com.example.Wime_java.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Wime_java.service.ExportService;
import com.example.Wime_java.service.ImportService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/import-export")
public class ImportExportController {

    @Autowired
    private ExportService exportService;

    @Autowired
    private ImportService importService;

    //---------------- EXPORTAR --------------------//
    @GetMapping("/export/{tipo}")
public ResponseEntity<InputStreamResource> exportar(
        @PathVariable String tipo,
        HttpSession session) throws IOException {

    Long idUsuario = (Long) session.getAttribute("id_usuario");
    if (idUsuario == null) {
        return ResponseEntity.status(401).body(null);
    }

    ByteArrayInputStream archivo = exportService.exportarDatos(tipo, idUsuario);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=" + tipo + ".csv");

    return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(new InputStreamResource(archivo));
}

    //---------------- IMPORTAR --------------------//
    @PostMapping("/import/{tipo}")
public ResponseEntity<String> importar(
        @PathVariable String tipo,
        @RequestParam("file") MultipartFile file,
        HttpSession session) {

    if (file.isEmpty()) {
        return ResponseEntity.badRequest().body("El archivo está vacío");
    }

    Long idUsuario = (Long) session.getAttribute("id_usuario");
    if (idUsuario == null) {
        return ResponseEntity.status(401).body("No hay sesión activa");
    }

    try {
        importService.importarDatos(tipo, file, idUsuario);
        return ResponseEntity.ok("Datos importados correctamente");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al procesar archivo: " + e.getMessage());
    }
}

}
