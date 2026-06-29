package com.transportista.sistemaguias.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class S3Service {
    
    @Autowired
    private S3Client s3Client;
    
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    
    public String subirArchivoAS3(String archivoPath, String transportista) throws IOException {
        // Verificar que el archivo existe
        Path archivoLocal = Paths.get(archivoPath);
        if (!Files.exists(archivoLocal)) {
            throw new IOException("El archivo no existe en la ruta: " + archivoPath);
        }

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fileName = archivoLocal.getFileName().toString();
        String s3Key = String.format("%s/%s/%s", transportista, fecha, fileName);
        
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType("application/pdf")
                .build();
        
        s3Client.putObject(putRequest, RequestBody.fromBytes(Files.readAllBytes(archivoLocal)));
        
        return s3Key;
    }
    
    public byte[] descargarDeS3(String s3Key) {
        if (s3Key == null || s3Key.isEmpty()) {
            throw new RuntimeException("La key de S3 es nula o vacía");
        }
        try {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            
            return s3Client.getObjectAsBytes(getRequest).asByteArray();
        } catch (S3Exception e) {
            throw new RuntimeException("No se pudo descargar el archivo de S3: " + e.getMessage());
        }
    }
    
    public void eliminarDeS3(String s3Key) {
        if (s3Key != null && !s3Key.isEmpty()) {
            try {
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Key)
                        .build();
                
                s3Client.deleteObject(deleteRequest);
            } catch (S3Exception e) {
                System.err.println("Error eliminando de S3: " + e.awsErrorDetails().errorMessage());
            }
        }
    }
}