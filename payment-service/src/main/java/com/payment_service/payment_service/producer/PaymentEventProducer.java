package com.payment_service.payment_service.producer;

import com.payment_service.payment_service.event.PaymentProcessedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate;

    public static String topic = "payment-completed";

    public void publish(PaymentProcessedEvent paymentProcessedEvent){

        kafkaTemplate.send(topic, paymentProcessedEvent);


    }
}
