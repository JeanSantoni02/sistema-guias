package com.transportista.sistemaguias.controller;

import com.transportista.sistemaguias.model.GuiaOracle;
import com.transportista.sistemaguias.service.MessageConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
public class MessageController {

    @Autowired
    private MessageConsumerService messageConsumerService;

    /**
     * Endpoint para consumir mensajes de la Cola 1 y guardar en Oracle
     * GET /api/mensajes/consumir
     */
    @GetMapping("/consumir")
    public String consumirMensajes() {
        messageConsumerService.consumirMensajes();
        return "✅ Mensajes consumidos y guardados en Oracle Cloud correctamente.";
    }

    /**
     * Endpoint para ver las guías guardadas en Oracle
     * GET /api/mensajes/oracle
     */
    @GetMapping("/oracle")
    public List<GuiaOracle> getGuiasOracle() {
        return messageConsumerService.getGuiasOracle();
    }
}