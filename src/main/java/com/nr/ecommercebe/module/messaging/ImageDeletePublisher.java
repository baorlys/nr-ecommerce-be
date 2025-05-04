package com.nr.ecommercebe.module.messaging;

import com.nr.ecommercebe.shared.event.ImageDeleteEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageDeletePublisher {
    AmqpTemplate amqpTemplate;

    public void publish(String imgUrl) {
        ImageDeleteEvent event = new ImageDeleteEvent(imgUrl);
        amqpTemplate.convertAndSend(
                RabbitConfig.IMAGE_DELETE_EXCHANGE,
                RabbitConfig.IMAGE_DELETE_ROUTING_KEY,
                event);
    }
}
