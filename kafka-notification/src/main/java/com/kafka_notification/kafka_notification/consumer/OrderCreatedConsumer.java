package com.kafka_notification.kafka_notification.consumer;

import com.kafka_notification.kafka_notification.event.OrderCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service

public class OrderCreatedConsumer {

    @KafkaListener(
            topics =  "order-created",
            groupId = "notification-group"
    )
    public void consume(OrderCreatedEvent orderCreatedEvent) {

        System.out.println("Order Created successfully for: " + orderCreatedEvent.getUsername());
    }

}
