package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponse {

    private Long orderId;
    private Long customerId;
    private String origin;
    private String destination;
    private double weight;
    private String status;
}
