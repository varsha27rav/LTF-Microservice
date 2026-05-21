package com.cts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentRequest {

    @NotNull(message = "CustomerId cannot be null")
    private Long customerId;

    @NotBlank(message = "Origin cannot be empty")
    private String origin;

    @NotBlank(message = "Destination cannot be empty")
    private String destination;

    @Positive(message = "Weight must be greater than 0")
    private double weight;
}