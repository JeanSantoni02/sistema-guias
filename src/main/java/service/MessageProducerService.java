package com.transportista.sistemaguias.service;

import com.transportista.sistemaguias.model.Guia;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    /**
     * Envía un mensaje a la Cola 1 (principal)
     * Si falla, envía automáticamente a la Cola 2 (DLQ)
     */
    public void sendGuiaMessage(Guia guia) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, guia);
            System.out.println("✅ Mensaje enviado a Cola 1: " + guia.getNumeroGuia());
        } catch (Exception e) {
            System.err.println("❌ Error al enviar a Cola 1, enviando a DLQ (Cola 2): " + e.getMessage());
            // El mensaje se enviará automáticamente a DLQ por la configuración
            rabbitTemplate.convertAndSend(exchange, "dlq." + routingKey, guia);
        }
    }
}