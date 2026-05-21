package com.cts.repository;

import com.cts.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    // Get all tasks assigned to a specific user
    List<TaskEntity> findByAssignedToUserId(Long userId);

    // Get tasks by status: PENDING, IN_PROGRESS, COMPLETED
    List<TaskEntity> findByStatus(String status);
}
