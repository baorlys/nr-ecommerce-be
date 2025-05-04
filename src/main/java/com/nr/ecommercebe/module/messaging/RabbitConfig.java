package com.nr.ecommercebe.module.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String IMAGE_DELETE_QUEUE = "image.delete.queue";
    public static final String IMAGE_DELETE_EXCHANGE = "image.exchange";
    public static final String IMAGE_DELETE_ROUTING_KEY = "image.delete";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange imageExchange() {
        return new DirectExchange(IMAGE_DELETE_EXCHANGE);
    }

    @Bean
    public Queue imageDeleteQueue() {
        return new Queue(IMAGE_DELETE_QUEUE);
    }

    @Bean
    public Binding imageDeleteBinding() {
        return BindingBuilder
                .bind(imageDeleteQueue())
                .to(imageExchange())
                .with(IMAGE_DELETE_ROUTING_KEY);
    }
}
