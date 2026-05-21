package com.cts.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long notificationId;
    private Long userId;
    private Long entityId;
    private String message;

    // Category: SHIPMENT, VEHICLE, DRIVER, DELIVERY, MAINTENANCE
    private String category;

    // Status: UNREAD, READ
    private String status;

    private LocalDateTime createdAt;
}
