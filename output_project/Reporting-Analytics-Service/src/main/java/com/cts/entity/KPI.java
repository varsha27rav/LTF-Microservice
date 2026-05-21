package com.cts.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

// Key Performance Indicators for LogiTrack
// Examples: fleet utilization rate, on-time delivery rate, driver trip completion rate
@Entity
@Table(name = "kpi")
@Data
public class KPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kpiId;

    private String name;
    private String definition;

    // Target value the system aims to achieve
    private Double target;

    // Current actual value
    private Double currentValue;

    private LocalDateTime reportingPeriod;

    // Category: FLEET, DELIVERY, DRIVER, SHIPMENT, MAINTENANCE
    private String category;
}
