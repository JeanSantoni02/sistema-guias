package com.transportista.sistemaguias.dto;

import java.time.LocalDateTime;

public class GuiaDTO {
    private Long id;
    private String numeroGuia;
    private String transportista;
    private String destinatario;
    private String direccionDestino;
    private Double peso;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private String estado;
    private String rutaArchivoS3;
    
    // Constructor vacío
    public GuiaDTO() {}
    
    // Constructor con todos los parámetros
    public GuiaDTO(Long id, String numeroGuia, String transportista, String destinatario,
                   String direccionDestino, Double peso, String descripcion, 
                   LocalDateTime fechaCreacion, String estado, String rutaArchivoS3) {
        this.id = id;
        this.numeroGuia = numeroGuia;
        this.transportista = transportista;
        this.destinatario = destinatario;
        this.direccionDestino = direccionDestino;
        this.peso = peso;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.rutaArchivoS3 = rutaArchivoS3;
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
    
    public String getRutaArchivoS3() { return rutaArchivoS3; }
    public void setRutaArchivoS3(String rutaArchivoS3) { this.rutaArchivoS3 = rutaArchivoS3; }
}