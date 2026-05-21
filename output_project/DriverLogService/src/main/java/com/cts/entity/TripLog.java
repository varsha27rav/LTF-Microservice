package com.cts.entity;

import java.time.LocalDateTime;

import com.cts.constants.TripStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class TripLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @NotNull
    private Long driverId;

    @NotNull
    private Long vehicleId;

    @NotNull
    private Long routeId;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Positive(message = "Distance must be positive")
    private Double distanceKm;

    private String notes;

    @Enumerated(EnumType.STRING)
    private TripStatus status;
}