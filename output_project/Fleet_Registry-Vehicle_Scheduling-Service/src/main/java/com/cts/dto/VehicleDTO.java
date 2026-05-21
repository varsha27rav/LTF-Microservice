package com.cts.dto;

import com.cts.constants.VehicleStatus;
import lombok.Data;

@Data
public class VehicleDTO {
    private Long vehicleId;
    private VehicleStatus status;
}