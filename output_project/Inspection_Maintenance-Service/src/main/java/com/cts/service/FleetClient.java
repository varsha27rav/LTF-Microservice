package com.cts.service;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.dto.VehicleDTO;

@FeignClient(name = "FleetRegistryService", url = "http://localhost:8095")
public interface FleetClient {

	@GetMapping("/vehicles/{id}")
	VehicleDTO getVehicle(@PathVariable("id") Long id);

	@PutMapping("/vehicles/{id}/status")
	void updateVehicleStatus(@PathVariable("id") Long id, @RequestParam("status") String status);
}