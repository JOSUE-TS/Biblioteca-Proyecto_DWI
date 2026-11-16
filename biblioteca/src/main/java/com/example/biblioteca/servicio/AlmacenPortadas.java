package com.example.biblioteca.servicio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlmacenPortadas {
    private static final long MAX_BYTES = 5L * 1024 * 1024;
    private static final Logger logger = LoggerFactory.getLogger(AlmacenPortadas.class);
    private final Path directorio;

    public AlmacenPortadas(@Value("${biblioteca.portadas.directorio:uploads/portadas}") String ruta) {
        this.directorio = Path.of(ruta).toAbsolutePath().normalize();
    }

    public String guardar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecciona una imagen de portada");
        }
        if (archivo.getSize() > MAX_BYTES) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "La imagen no debe superar 5 MB");
        }

        try {
            byte[] contenido = archivo.getBytes();
            String extension = extensionDe(contenido);
            String nombre = UUID.randomUUID() + extension;
            Files.createDirectories(directorio);
            Files.write(directorio.resolve(nombre), contenido, StandardOpenOption.CREATE_NEW);
            return "/uploads/portadas/" + nombre;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo guardar la imagen de portada", ex);
        }
    }

    public void eliminar(String url) {
        if (url == null || !url.startsWith("/uploads/portadas/")) return;
        String nombre = url.substring("/uploads/portadas/".length());
        Path archivo = directorio.resolve(nombre).normalize();
        if (!archivo.startsWith(directorio)) return;
        try {
            Files.deleteIfExists(archivo);
        } catch (IOException ex) {
            logger.warn("No se pudo retirar la portada anterior {}", nombre, ex);
        }
    }

    private String extensionDe(byte[] bytes) {
        if (bytes.length >= 8 && (bytes[0] & 0xff) == 0x89 && bytes[1] == 0x50
                && bytes[2] == 0x4e && bytes[3] == 0x47 && bytes[4] == 0x0d
                && bytes[5] == 0x0a && bytes[6] == 0x1a && bytes[7] == 0x0a) return ".png";
        if (bytes.length >= 3 && (bytes[0] & 0xff) == 0xff && (bytes[1] & 0xff) == 0xd8
                && (bytes[2] & 0xff) == 0xff) return ".jpg";
        if (bytes.length >= 12 && new String(bytes, 0, 4).equals("RIFF")
                && new String(bytes, 8, 4).equals("WEBP")) return ".webp";
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Usa una imagen PNG, JPG o WEBP válida");
    }
}
