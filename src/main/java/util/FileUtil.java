package com.transportista.sistemaguias.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Clase de utilidades para manejo de archivos y operaciones comunes
 */
public class FileUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    /**
     * Genera un nombre único para un archivo
     * @param originalFilename Nombre original del archivo
     * @return Nombre único generado
     */
    public static String generarNombreUnico(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return String.format("guia_%s_%s%s", timestamp, uuid, extension);
    }
    
    /**
     * Obtiene la extensión de un archivo
     * @param filename Nombre del archivo
     * @return Extensión (ej: .pdf, .jpg)
     */
    public static String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
    
    /**
     * Valida que el archivo sea un PDF
     * @param file Archivo a validar
     * @return true si es PDF válido
     */
    public static boolean esPdfValido(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        
        return (contentType != null && contentType.equals("application/pdf")) ||
               (filename != null && filename.toLowerCase().endsWith(".pdf"));
    }
    
    /**
     * Obtiene el tamaño del archivo en formato legible
     * @param bytes Tamaño en bytes
     * @return String con formato (KB, MB, etc)
     */
    public static String getFileSizeReadable(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    /**
     * Limpia el nombre de un archivo para que sea seguro
     * @param filename Nombre original
     * @return Nombre limpio sin caracteres especiales
     */
    public static String sanitizeFilename(String filename) {
        if (filename == null) return "archivo";
        return filename.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}