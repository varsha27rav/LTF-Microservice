package com.cts.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class KPIDTO {
    private Long kpiId;
    private String name;
    private String definition;
    private Double target;
    private Double currentValue;
    private LocalDateTime reportingPeriod;

    // Category: FLEET, DELIVERY, DRIVER, SHIPMENT, MAINTENANCE
    private String category;
}
