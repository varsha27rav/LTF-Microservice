package com.cts.entity;

import java.time.LocalDateTime;

import com.cts.constants.MaintenanceStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintId;

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotBlank(message = "Task description is required")
    private String taskDescription;

    @NotBlank(message = "Performed by is required")
    private String performedBy;

    private LocalDateTime performedAt;

    @Positive(message = "Cost must be positive")
    private Double cost;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;
}