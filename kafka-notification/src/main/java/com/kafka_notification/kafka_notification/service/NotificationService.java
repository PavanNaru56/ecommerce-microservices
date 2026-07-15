package com.kafka_notification.kafka_notification.service;

import com.kafka_notification.kafka_notification.event.OrderCreatedEvent;
import com.kafka_notification.kafka_notification.model.Notification;
import com.kafka_notification.kafka_notification.model.NotificationStatus;
import com.kafka_notification.kafka_notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void saveNotification(OrderCreatedEvent event){

        Notification notification = Notification.builder()
                .recipient(event.getUsername())
                .messsage("Order created successfully. Order ID: " + event.getOrderId())
                .status(NotificationStatus.SENT)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }
}
