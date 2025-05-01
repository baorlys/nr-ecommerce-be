package com.nr.ecommercebe.module.messaging;

import com.nr.ecommercebe.shared.event.ImageDeleteEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageDeletePublisher {
    RabbitTemplate rabbitTemplate;

    public void publish(String imageUrl) {
        ImageDeleteEvent event = new ImageDeleteEvent(imageUrl);
        rabbitTemplate.convertAndSend("image.exchange", "image.delete", event);
    }
}
