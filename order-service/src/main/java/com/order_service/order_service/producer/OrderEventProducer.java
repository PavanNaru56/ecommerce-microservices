package com.order_service.order_service.producer;

import com.order_service.order_service.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    private final String TOPIC = "order-created";

    public void publishOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {

        kafkaTemplate.send(TOPIC, orderCreatedEvent);

        System.out.println("publish order event: " + orderCreatedEvent);
    }
}
