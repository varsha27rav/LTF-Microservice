package com.cts.service;

import com.cts.dto.NotificationDTO;
import java.util.List;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO dto);
    List<NotificationDTO> getAllNotifications();
    List<NotificationDTO> getNotificationsByUser(Long userId);
    List<NotificationDTO> getUnreadByUser(Long userId);
    NotificationDTO getNotificationById(Long id);
    NotificationDTO markAsRead(Long id);
    void deleteNotification(Long id);
}
