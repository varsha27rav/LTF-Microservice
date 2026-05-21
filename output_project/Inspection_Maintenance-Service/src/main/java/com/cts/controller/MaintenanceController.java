package com.cts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.cts.entity.MaintenanceRecord;
import com.cts.service.MaintenanceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService service;

    @PostMapping("/schedule")
    public MaintenanceRecord schedule(@Valid @RequestBody MaintenanceRecord m) {
        return service.schedule(m);
    }

    @PostMapping("/start")
    public MaintenanceRecord start(@Valid @RequestBody MaintenanceRecord m) {
        return service.start(m);
    }

    @PutMapping("/{id}/complete")
    public MaintenanceRecord complete(@PathVariable Long id) {
        return service.complete(id);
    }

    @PutMapping("/{id}/cancel")
    public MaintenanceRecord cancel(@PathVariable Long id) {
        return service.cancel(id);
    }

    @GetMapping
    public List<MaintenanceRecord> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public MaintenanceRecord get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<MaintenanceRecord> byVehicle(@PathVariable Long vehicleId) {
        return service.byVehicle(vehicleId);
    }
}