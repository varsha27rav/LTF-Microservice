package com.cts.dto;

import com.cts.constants.VehicleStatus;

import lombok.Data;

@Data
public class DriverVehicleDTO {

	private Long vehicleId;
	private String regNumber;
	private String type;
	private Integer capacity;
	private VehicleStatus status;

}
