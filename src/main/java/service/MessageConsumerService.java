package com.transportista.sistemaguias.service;

import com.transportista.sistemaguias.model.Guia;
import com.transportista.sistemaguias.model.GuiaOracle;
import com.transportista.sistemaguias.repository.GuiaOracleRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageConsumerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private GuiaOracleRepository guiaOracleRepository;

    @Value("${rabbitmq.queue.principal}")
    private String principalQueue;

    /**
     * Consume todos los mensajes de la Cola 1 y los guarda en Oracle Cloud
     */
    @Transactional
    public void consumirMensajes() {
        List<Guia> guiasConsumidos = new ArrayList<>();
        
        // Recibir todos los mensajes de la cola
        Object message = rabbitTemplate.receiveAndConvert(principalQueue);
        
        while (message != null) {
            if (message instanceof Guia guia) {
                // Guardar en Oracle Cloud
                GuiaOracle guiaOracle = new GuiaOracle(guia);
                guiaOracleRepository.save(guiaOracle);
                guiasConsumidos.add(guia);
                
                System.out.println("✅ Mensaje consumido y guardado en Oracle: " + guia.getNumeroGuia());
            }
            message = rabbitTemplate.receiveAndConvert(principalQueue);
        }
        
        if (guiasConsumidos.isEmpty()) {
            System.out.println("📭 No hay mensajes en la cola para consumir.");
        } else {
            System.out.println("📊 Total mensajes consumidos: " + guiasConsumidos.size());
        }
    }

    /**
     * Obtiene todas las guías guardadas en Oracle
     */
    public List<GuiaOracle> getGuiasOracle() {
        return guiaOracleRepository.findAll();
    }
}
