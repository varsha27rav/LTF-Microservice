package com.cts.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.constants.DriverStatus;
import com.cts.entity.Driver;
import com.cts.entity.TripLog;
import com.cts.service.DriverService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService service;

    @PostMapping
    public Driver create(@Valid @RequestBody Driver d) {
        return service.create(d);
    }

    @GetMapping("/{id}")
    public Driver get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<Driver> all() {
        return service.getAll();
    }

    @GetMapping("/status/{status}")
    public List<Driver> byStatus(@PathVariable DriverStatus status) {
        return service.getByStatus(status);
    }

    @PutMapping("/{id}")
    public Driver update(@PathVariable Long id, @RequestBody Driver d) {
        return service.update(id, d);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PatchMapping("/{id}/status")
    public Driver changeStatus(@PathVariable Long id, @RequestParam DriverStatus status) {
        return service.changeStatus(id, status);
    }

    @GetMapping("/{id}/availability")
    public boolean availability(@PathVariable Long id) {
        return service.isAvailable(id);
    }

    @GetMapping("/{id}/trips")
    public List<TripLog> trips(@PathVariable Long id) {
        return service.getDriverTrips(id);
    }

    @GetMapping("/{id}/duty-hours")
    public double duty(@PathVariable Long id) {
        return service.getDutyHours(id);
    }

    @GetMapping("/{id}/active-assignment")
    public Optional<?> active(@PathVariable Long id) {
        return service.getActiveAssignment(id);
    }
}