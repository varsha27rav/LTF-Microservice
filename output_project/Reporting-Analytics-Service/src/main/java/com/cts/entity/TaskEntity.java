package com.cts.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

// Tasks assigned to staff members in LogiTrack
// Examples: "Schedule maintenance for vehicle V-101"
// "Follow up on delayed shipment S-202"
// "Review driver trip log for driver D-55"
@Entity
@Table(name = "task")
@Data
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    // Who this task is assigned to (userId from Admin-Service)
    private Long assignedToUserId;

    // The related entity (shipmentId, vehicleId, driverId etc.)
    private Long relatedEntityId;

    private String description;
    private LocalDate dueDate;

    // Status: PENDING, IN_PROGRESS, COMPLETED
    private String status;
}
