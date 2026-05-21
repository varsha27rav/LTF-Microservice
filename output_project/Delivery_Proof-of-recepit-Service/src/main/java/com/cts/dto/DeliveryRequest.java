package com.cts.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeliveryRequest {
	@NotNull(message = "Order id should not be blank")
    private Long orderId;
	
	@NotNull(message = "Vehicle id should not be blank")
    private Long vehicleId;
	
	@NotNull(message = "Driver id should not be blank")
    private Long driverId;
}