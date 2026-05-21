package com.cts.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "DriverLogService", url = "http://localhost:9091/trips")
public interface TripClient {

    @PutMapping("/cancel-by-vehicle/{vehicleId}")
    void cancelTrips(@PathVariable Long vehicleId);
}