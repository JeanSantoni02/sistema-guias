package com.transportista.sistemaguias.repository;

import com.transportista.sistemaguias.model.Guia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GuiaRepository extends JpaRepository<Guia, Long> {
    
    // Buscar guías por transportista y fecha
    List<Guia> findByTransportistaAndFechaKey(String transportista, String fechaKey);
    
    // Buscar guías por transportista
    List<Guia> findByTransportista(String transportista);
    
    // Buscar guías por fecha
    List<Guia> findByFechaKey(String fechaKey);
    
    // Verificar si existe un número de guía
    boolean existsByNumeroGuia(String numeroGuia);
}