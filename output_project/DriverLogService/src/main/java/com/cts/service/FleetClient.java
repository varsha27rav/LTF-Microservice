package com.cts.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.dto.VehicleDTO;

@FeignClient(name = "FleetRegistryService", url = "http://localhost:8095/vehicles")
public interface FleetClient {

    @GetMapping("/{id}")
    VehicleDTO getVehicle(@PathVariable Long id);
}
