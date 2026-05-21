package com.cts.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long taskId;
    private Long assignedToUserId;
    private Long relatedEntityId;
    private String description;
    private LocalDate dueDate;

    // Status: PENDING, IN_PROGRESS, COMPLETED
    private String status;
}
