package com.transportista.sistemaguias.service;

import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class S3Service {
    
    public String subirArchivoAS3(String archivoPath, String transportista) throws IOException {
        System.out.println("==========================================");
        System.out.println("S3 Service: MODO DEMO - Sin AWS configurado");
        System.out.println("Archivo simulado: " + archivoPath);
        System.out.println("Transportista: " + transportista);
        System.out.println("==========================================");
        return "s3://bucket-demo/" + transportista + "/" + System.currentTimeMillis() + ".pdf";
    }
    
    public byte[] descargarDeS3(String s3Key) {
        System.out.println("==========================================");
        System.out.println("S3 Service: MODO DEMO - Descarga simulada");
        System.out.println("S3 Key solicitada: " + s3Key);
        System.out.println("==========================================");
        // Retorna un PDF de prueba vacío
        return new byte[0];
    }
    
    public void eliminarDeS3(String s3Key) {
        System.out.println("==========================================");
        System.out.println("S3 Service: MODO DEMO - Eliminación simulada");
        System.out.println("S3 Key eliminada: " + s3Key);
        System.out.println("==========================================");
    }
}