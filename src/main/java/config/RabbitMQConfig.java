package com.transportista.sistemaguias.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.principal}")
    private String principalQueue;

    @Value("${rabbitmq.queue.dlq}")
    private String dlq;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    /**
     * Cola principal (Cola 1)
     * Si falla, los mensajes van a DLQ (Cola 2)
     */
    @Bean
    public Queue principalQueue() {
        return QueueBuilder.durable(principalQueue)
                .deadLetterExchange(exchange)
                .deadLetterRoutingKey("dlq." + routingKey)
                .build();
    }

    /**
     * Cola de mensajes fallidos (Cola 2 - DLQ)
     */
    @Bean
    public Queue dlq() {
        return QueueBuilder.durable(dlq).build();
    }

    /**
     * Exchange para enrutar mensajes
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    /**
     * Binding de la cola principal al exchange
     */
    @Bean
    public Binding binding(Queue principalQueue, DirectExchange exchange) {
        return BindingBuilder.bind(principalQueue).to(exchange).with(routingKey);
    }

    /**
     * Binding de la DLQ al exchange
     */
    @Bean
    public Binding dlqBinding(Queue dlq, DirectExchange exchange) {
        return BindingBuilder.bind(dlq).to(exchange).with("dlq." + routingKey);
    }

    /**
     * Convertidor de mensajes a JSON
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Template para enviar mensajes
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}