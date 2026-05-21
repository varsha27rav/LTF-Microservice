package com.cts.repository;

import com.cts.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    // Get all notifications for a specific user
    List<NotificationEntity> findByUserId(Long userId);

    // Get notifications filtered by user and read status
    List<NotificationEntity> findByUserIdAndStatus(Long userId, String status);
}
