package com.kafka_notification.kafka_notification.repository;

import com.kafka_notification.kafka_notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
