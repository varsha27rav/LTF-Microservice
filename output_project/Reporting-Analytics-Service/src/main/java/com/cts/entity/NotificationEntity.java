package com.cts.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

// In-app notifications for LogiTrack users
// Created when important events happen:
// shipment dispatched, delivery completed, vehicle maintenance due, driver assigned etc.
@Entity
@Table(name = "notifications")
@Data
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    // Who receives this notification (userId from Admin-Service)
    private Long userId;

    // The related entity ID (shipmentId, vehicleId, tripId etc.)
    private Long entityId;

    private String message;

    // Category: SHIPMENT, VEHICLE, DRIVER, DELIVERY, MAINTENANCE
    private String category;

    // Status: UNREAD, READ
    private String status;

    private LocalDateTime createdAt;
}
