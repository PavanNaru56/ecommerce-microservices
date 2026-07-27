package com.kafka_notification.kafka_notification.controller;

import com.kafka_notification.kafka_notification.model.Notification;
import com.kafka_notification.kafka_notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private NotificationService notificationService;
    NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getAllNotifications() {

        List<Notification> notifications = notificationService.getAllNotifications();

        return ResponseEntity.status(HttpStatus.OK).body(notifications);
    }
}
