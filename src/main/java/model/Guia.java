package com.transportista.sistemaguias.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "guias")
public class Guia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String numeroGuia;
    
    @Column(nullable = false, length = 100)
    private String transportista;
    
    @Column(nullable = false, length = 100)
    private String destinatario;
    
    @Column(nullable = false, length = 200)
    private String direccionDestino;
    
    @Column(nullable = false)
    private Double peso;
    
    @Column(nullable = false, length = 500)
    private String descripcion;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(nullable = false, length = 50)
    private String estado;
    
    @Column(length = 500)
    private String rutaArchivoEfs;
    
    @Column(length = 500)
    private String rutaArchivoS3;
    
    @Column(nullable = false, length = 20)
    private String fechaKey;
    
    // Constructor vacío
    public Guia() {}
    
    // Constructor con parámetros
    public Guia(String numeroGuia, String transportista, String destinatario, 
                String direccionDestino, Double peso, String descripcion) {
        this.numeroGuia = numeroGuia;
        this.transportista = transportista;
        this.destinatario = destinatario;
        this.direccionDestino = direccionDestino;
        this.peso = peso;
        this.descripcion = descripcion;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "PENDIENTE";
        this.fechaKey = LocalDate.now().toString();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }
    
    public String getTransportista() { return transportista; }
    public void setTransportista(String transportista) { this.transportista = transportista; }
    
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    
    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }
    
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getRutaArchivoEfs() { return rutaArchivoEfs; }
    public void setRutaArchivoEfs(String rutaArchivoEfs) { this.rutaArchivoEfs = rutaArchivoEfs; }
    
    public String getRutaArchivoS3() { return rutaArchivoS3; }
    public void setRutaArchivoS3(String rutaArchivoS3) { this.rutaArchivoS3 = rutaArchivoS3; }
    
    public String getFechaKey() { return fechaKey; }
    public void setFechaKey(String fechaKey) { this.fechaKey = fechaKey; }
}