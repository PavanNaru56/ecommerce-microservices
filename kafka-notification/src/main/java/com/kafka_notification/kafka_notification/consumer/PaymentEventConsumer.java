package com.kafka_notification.kafka_notification.consumer;

import com.kafka_notification.kafka_notification.event.PaymentProcessedEvent;
import lombok.NoArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class PaymentEventConsumer {

    @KafkaListener(
            topics = "payment-completed",
            groupId = "notification-group"
    )
    public void consume(PaymentProcessedEvent paymentProcessedEvent) {
        System.out.println("Received Payment Event: " + paymentProcessedEvent);
    }

}

