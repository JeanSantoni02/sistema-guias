package com.transportista.sistemaguias.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * Modelo para la tabla en Oracle Cloud (tabla separada)
 */
@Entity
@Table(name = "guias_oracle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuiaOracle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroGuia;

    @Column(nullable = false)
    private String transportista;

    @Column(nullable = false)
    private String destinatario;

    @Column(nullable = false)
    private String direccionDestino;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private String estado;

    private String rutaArchivoS3;

    // Constructor para copiar desde Guia
    public GuiaOracle(Guia guia) {
        this.numeroGuia = guia.getNumeroGuia();
        this.transportista = guia.getTransportista();
        this.destinatario = guia.getDestinatario();
        this.direccionDestino = guia.getDireccionDestino();
        this.peso = guia.getPeso();
        this.descripcion = guia.getDescripcion();
        this.fechaCreacion = guia.getFechaCreacion();
        this.estado = guia.getEstado();
        this.rutaArchivoS3 = guia.getRutaArchivoS3();
    }
}