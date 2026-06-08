package com.transportista.sistemaguias.controller;

import com.transportista.sistemaguias.dto.GuiaDTO;
import com.transportista.sistemaguias.service.GuiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/guias")
@CrossOrigin(origins = "*") // Para permitir peticiones desde cualquier origen
public class GuiaController {
    
    @Autowired
    private GuiaService guiaService;
    
    /**
     * 1. Crear guía de despacho
     * POST /api/guias
     * Form-data: archivo (PDF), transportista, destinatario, direccionDestino, peso, descripcion
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GuiaDTO> crearGuia(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("transportista") String transportista,
            @RequestParam("destinatario") String destinatario,
            @RequestParam("direccionDestino") String direccionDestino,
            @RequestParam("peso") Double peso,
            @RequestParam("descripcion") String descripcion) throws IOException {
        
        GuiaDTO nuevaGuia = guiaService.crearGuia(archivo, transportista, destinatario, 
                                                    direccionDestino, peso, descripcion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaGuia);
    }
    
    /**
     * 2. Subir guía generada a S3 (moverla desde EFS)
     * POST /api/guias/{id}/subir-a-s3
     */
    @PostMapping("/{id}/subir-a-s3")
    public ResponseEntity<String> subirGuiaAS3(@PathVariable Long id) throws IOException {
        guiaService.subirGuiaAS3(id);
        return ResponseEntity.ok("Guía subida exitosamente a S3");
    }
    
    /**
     * 3. Descargar guía con validación de permisos
     * GET /api/guias/{id}/descargar?usuario=nombre
     */
    @GetMapping("/{id}/descargar")
    public ResponseEntity<byte[]> descargarGuia(
            @PathVariable Long id,
            @RequestParam("usuario") String usuario) throws IOException {
        
        byte[] contenido = guiaService.descargarGuia(id, usuario);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=guia_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(contenido);
    }
    
    /**
     * 4. Modificar o actualizar guía
     * PUT /api/guias/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<GuiaDTO> actualizarGuia(@PathVariable Long id, @RequestBody GuiaDTO guiaDTO) {
        GuiaDTO guiaActualizada = guiaService.actualizarGuia(id, guiaDTO);
        return ResponseEntity.ok(guiaActualizada);
    }
    
    /**
     * 5. Eliminar guía específica
     * DELETE /api/guias/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGuia(@PathVariable Long id) {
        guiaService.eliminarGuia(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 6. Consultar guías por transportista y fecha
     * GET /api/guias/consultar?transportista=xxx&fecha=2024-01-15
     * GET /api/guias/consultar?transportista=xxx
     * GET /api/guias/consultar?fecha=2024-01-15
     * GET /api/guias/consultar (todas)
     */
    @GetMapping("/consultar")
    public ResponseEntity<List<GuiaDTO>> consultarGuias(
            @RequestParam(required = false) String transportista,
            @RequestParam(required = false) String fecha) {
        
        List<GuiaDTO> guias = guiaService.consultarGuias(transportista, fecha);
        return ResponseEntity.ok(guias);
    }
    
    /**
     * Obtener una guía por ID (sin descargar archivo)
     * GET /api/guias/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<GuiaDTO> obtenerGuia(@PathVariable Long id) {
        GuiaDTO guia = guiaService.obtenerGuia(id);
        return ResponseEntity.ok(guia);
    }
    
    /**
     * Listar todas las guías
     * GET /api/guias
     */
    @GetMapping
    public ResponseEntity<List<GuiaDTO>> listarTodas() {
        List<GuiaDTO> guias = guiaService.listarTodas();
        return ResponseEntity.ok(guias);
    }
}