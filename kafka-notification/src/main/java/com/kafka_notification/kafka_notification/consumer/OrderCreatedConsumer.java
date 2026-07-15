package com.kafka_notification.kafka_notification.consumer;

import com.kafka_notification.kafka_notification.event.OrderCreatedEvent;
import com.kafka_notification.kafka_notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics =  "order-created",
            groupId = "notification-group"
    )



    public void consume(OrderCreatedEvent orderCreatedEvent) {


        notificationService.saveNotification(orderCreatedEvent);

        System.out.println("Order Created successfully for: " + orderCreatedEvent.getUsername());
    }

}
