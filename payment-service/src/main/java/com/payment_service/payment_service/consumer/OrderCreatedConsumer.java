package com.payment_service.payment_service.consumer;

import com.payment_service.payment_service.event.OrderCreatedEvent;
import com.payment_service.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final PaymentService paymentService;

    @KafkaListener(
            topics = "order-created",
            groupId = "payment-group"
    )
    public void consume(OrderCreatedEvent orderCreatedEvent) {

        System.out.println("OrderCreatedEvent: " + orderCreatedEvent.getUsername());

        paymentService.savePaymentAuto(orderCreatedEvent);

    }
}
