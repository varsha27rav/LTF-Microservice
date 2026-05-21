package com.cts.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.dto.UserIdDTO;

@FeignClient(name = "Admin-Service", url = "http://localhost:8078")
public interface APIClient {

	@GetMapping("/api/admin/shipment/users/{id}")
	UserIdDTO getUser(@PathVariable("id") Long id);
}
