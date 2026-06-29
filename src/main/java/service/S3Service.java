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
    
    @Value("${aws.s3.region:us-east-1}")
    private String region;
    
    public String subirArchivoAS3(String archivoPath, String transportista) throws IOException {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Path archivoLocal = Paths.get(archivoPath);
        String fileName = archivoLocal.getFileName().toString();
        String s3Key = String.format("%s/%s/%s", transportista, fecha, fileName);
        
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType("application/pdf")
                .build();
        
        s3Client.putObject(putRequest, RequestBody.fromBytes(Files.readAllBytes(archivoLocal)));
        
        System.out.println("Archivo subido a S3: " + s3Key);
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
            System.err.println("Error descargando de S3: " + e.awsErrorDetails().errorMessage());
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
                System.out.println("Archivo eliminado de S3: " + s3Key);
            } catch (S3Exception e) {
                System.err.println("Error eliminando de S3: " + e.awsErrorDetails().errorMessage());
            }
        }
    }
}