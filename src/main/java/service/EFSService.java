package com.transportista.sistemaguias.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@Service
public class EFSService {
    
    @Value("${app.efs.mount-path:/app/efs}")
    private String efsMountPath;
    
    public String guardarTemporalmente(MultipartFile archivo, String numeroGuia) throws IOException {
        Path efsDirectory = Paths.get(efsMountPath);
        
        if (!Files.exists(efsDirectory)) {
            Files.createDirectories(efsDirectory);
            System.out.println("Directorio EFS creado: " + efsMountPath);
        }
        
        String originalFilename = archivo.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String fileName = "guia_" + numeroGuia + "_" + System.currentTimeMillis() + extension;
        Path filePath = efsDirectory.resolve(fileName);
        
        Files.copy(archivo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("Archivo guardado en EFS: " + filePath.toString());
        return filePath.toString();
    }
    
    public byte[] leerDeEfs(String rutaArchivo) throws IOException {
        if (rutaArchivo == null || rutaArchivo.isEmpty()) {
            throw new IOException("Ruta de archivo nula o vacía");
        }
        
        Path filePath = Paths.get(rutaArchivo);
        if (Files.exists(filePath) && Files.isReadable(filePath)) {
            return Files.readAllBytes(filePath);
        }
        throw new IOException("Archivo no encontrado en EFS: " + rutaArchivo);
    }
    
    public void eliminarDeEfs(String rutaArchivo) throws IOException {
        if (rutaArchivo != null && !rutaArchivo.isEmpty()) {
            Path filePath = Paths.get(rutaArchivo);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Archivo eliminado de EFS: " + rutaArchivo);
            }
        }
    }
    
    public boolean existeEnEfs(String rutaArchivo) {
        if (rutaArchivo == null) return false;
        return Files.exists(Paths.get(rutaArchivo));
    }
}