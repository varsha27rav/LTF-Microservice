package com.cts.controller;

import com.cts.dto.NotificationDTO;
import com.cts.service.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reporting/notifications")
public class NotificationController {

    @Autowired
    private NotificationServiceImpl notificationService;

    // ADMIN, MANAGER, or DISPATCHER can create notifications
    @PostMapping
    public ResponseEntity<?> createNotification(
            @RequestHeader("X-User-Role") String role,
            @RequestBody NotificationDTO dto) {

        if (!role.equals("ADMIN") && !role.equals("MANAGER") && !role.equals("DISPATCHER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role to create notifications");
        }
        return new ResponseEntity<>(notificationService.createNotification(dto), HttpStatus.CREATED);
    }

    // Only ADMIN can view all notifications
    @GetMapping
    public ResponseEntity<?> getAllNotifications(
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can view all notifications");
        }
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    // Any authenticated user can view their own notifications
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotificationsByUser(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long userId) {

        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    // Get only unread notifications for a user
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<?> getUnreadByUser(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long userId) {

        return ResponseEntity.ok(notificationService.getUnreadByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    // Mark a notification as read when user views it
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    // Only ADMIN can delete notifications
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete notifications");
        }
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notification deleted successfully");
    }
}
