package com.cts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.entity.InspectionRecord;
import com.cts.service.InspectionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inspections")
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionService service;

    @PostMapping
    public InspectionRecord create(@Valid @RequestBody InspectionRecord r) {
        return service.create(r);
    }

    @PutMapping("/{id}")
    public InspectionRecord update(@PathVariable Long id,
                                   @Valid @RequestBody InspectionRecord r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    public List<InspectionRecord> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public InspectionRecord get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<InspectionRecord> byVehicle(@PathVariable Long vehicleId) {
        return service.byVehicle(vehicleId);
    }
}