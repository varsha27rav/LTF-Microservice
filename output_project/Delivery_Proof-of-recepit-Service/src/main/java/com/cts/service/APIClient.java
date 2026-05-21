package com.cts.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.dto.StatusUpdateRequest;

@FeignClient(name = "Shipment-Service", url = "http://localhost:8096/api/shipment")
public interface APIClient {

    // ✅ Fetch OTP for validation
    @GetMapping("/{orderId}/otp")
    String getOtp(@PathVariable Long orderId);

    // ✅ Update Shipment Status (CLOSED)
    @PutMapping("/{orderId}/status")
    String updateShipmentStatus(@PathVariable Long orderId,
                               @RequestBody StatusUpdateRequest request);
}