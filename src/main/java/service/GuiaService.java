package com.transportista.sistemaguias.service;

import com.transportista.sistemaguias.dto.GuiaDTO;
import com.transportista.sistemaguias.model.Guia;
import com.transportista.sistemaguias.repository.GuiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GuiaService {
    
    @Autowired
    private GuiaRepository guiaRepository;
    
    @Autowired
    private EFSService efsService;
    
    @Autowired
    private S3Service s3Service;
    
    @Transactional
    public GuiaDTO crearGuia(MultipartFile archivo, String transportista, String destinatario,
                             String direccionDestino, Double peso, String descripcion) throws IOException {
        
        String numeroGuia = "GUI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Guia guia = new Guia(numeroGuia, transportista, destinatario, 
                             direccionDestino, peso, descripcion);
        
        String rutaEfs = efsService.guardarTemporalmente(archivo, numeroGuia);
        guia.setRutaArchivoEfs(rutaEfs);
        
        Guia savedGuia = guiaRepository.save(guia);
        
        return convertToDTO(savedGuia);
    }
    
    @Transactional
    public GuiaDTO subirGuiaAS3(Long guiaId) throws IOException {
        Guia guia = guiaRepository.findById(guiaId)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada con ID: " + guiaId));
        
        if (guia.getRutaArchivoS3() != null) {
            throw new RuntimeException("La guía ya fue subida a S3");
        }
        
        String s3Key = s3Service.subirArchivoAS3(guia.getRutaArchivoEfs(), guia.getTransportista());
        guia.setRutaArchivoS3(s3Key);
        
        Guia updatedGuia = guiaRepository.save(guia);
        return convertToDTO(updatedGuia);
    }
    
    public byte[] descargarGuia(Long guiaId, String usuarioAutenticado) throws IOException {
        Guia guia = guiaRepository.findById(guiaId)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));
        
        if (!guia.getTransportista().equals(usuarioAutenticado) && !"ADMIN".equals(usuarioAutenticado)) {
            throw new SecurityException("No tienes permisos para descargar esta guía");
        }
        
        if (guia.getRutaArchivoEfs() != null && efsService.existeEnEfs(guia.getRutaArchivoEfs())) {
            try {
                return efsService.leerDeEfs(guia.getRutaArchivoEfs());
            } catch (IOException e) {
                System.out.println("Error leyendo de EFS, intentando S3: " + e.getMessage());
            }
        }
        
        if (guia.getRutaArchivoS3() != null) {
            return s3Service.descargarDeS3(guia.getRutaArchivoS3());
        }
        
        throw new IOException("No se encontró el archivo de la guía");
    }
    
    @Transactional
    public GuiaDTO actualizarGuia(Long guiaId, GuiaDTO guiaDTO) {
        Guia guia = guiaRepository.findById(guiaId)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));
        
        if (guiaDTO.getDestinatario() != null) guia.setDestinatario(guiaDTO.getDestinatario());
        if (guiaDTO.getDireccionDestino() != null) guia.setDireccionDestino(guiaDTO.getDireccionDestino());
        if (guiaDTO.getPeso() != null) guia.setPeso(guiaDTO.getPeso());
        if (guiaDTO.getDescripcion() != null) guia.setDescripcion(guiaDTO.getDescripcion());
        if (guiaDTO.getEstado() != null) guia.setEstado(guiaDTO.getEstado());
        
        Guia updatedGuia = guiaRepository.save(guia);
        return convertToDTO(updatedGuia);
    }
    
    @Transactional
    public void eliminarGuia(Long guiaId) {
        Guia guia = guiaRepository.findById(guiaId)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));
        
        try {
            if (guia.getRutaArchivoEfs() != null) {
                efsService.eliminarDeEfs(guia.getRutaArchivoEfs());
            }
            if (guia.getRutaArchivoS3() != null) {
                s3Service.eliminarDeS3(guia.getRutaArchivoS3());
            }
        } catch (IOException e) {
            System.err.println("Error al eliminar archivos: " + e.getMessage());
        }
        
        guiaRepository.delete(guia);
    }
    
    public List<GuiaDTO> consultarGuias(String transportista, String fecha) {
        List<Guia> guias;
        
        if (transportista != null && !transportista.isEmpty() && fecha != null && !fecha.isEmpty()) {
            guias = guiaRepository.findByTransportistaAndFechaKey(transportista, fecha);
        } else if (transportista != null && !transportista.isEmpty()) {
            guias = guiaRepository.findByTransportista(transportista);
        } else if (fecha != null && !fecha.isEmpty()) {
            guias = guiaRepository.findByFechaKey(fecha);
        } else {
            guias = guiaRepository.findAll();
        }
        
        return guias.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    public GuiaDTO obtenerGuia(Long id) {
        Guia guia = guiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));
        return convertToDTO(guia);
    }
    
    public List<GuiaDTO> listarTodas() {
        return guiaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private GuiaDTO convertToDTO(Guia guia) {
        GuiaDTO dto = new GuiaDTO();
        dto.setId(guia.getId());
        dto.setNumeroGuia(guia.getNumeroGuia());
        dto.setTransportista(guia.getTransportista());
        dto.setDestinatario(guia.getDestinatario());
        dto.setDireccionDestino(guia.getDireccionDestino());
        dto.setPeso(guia.getPeso());
        dto.setDescripcion(guia.getDescripcion());
        dto.setFechaCreacion(guia.getFechaCreacion());
        dto.setEstado(guia.getEstado());
        dto.setRutaArchivoS3(guia.getRutaArchivoS3());
        return dto;
    }
}
