package com.nr.ecommercebe.module.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public DirectExchange imageExchange() {
        return new DirectExchange("image.exchange");
    }

    @Bean
    public Queue imageDeleteQueue() {
        return new Queue("image.delete.queue");
    }

    @Bean
    public Binding imageDeleteBinding() {
        return BindingBuilder
                .bind(imageDeleteQueue())
                .to(imageExchange())
                .with("image.delete");
    }
}
