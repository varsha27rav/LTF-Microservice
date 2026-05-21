package com.cts.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cts.constants.MaintenanceStatus;
import com.cts.constants.VehicleStatus;
import com.cts.dto.VehicleDTO;
import com.cts.entity.MaintenanceRecord;
import com.cts.exception.BadRequestException;
import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.InspectionRepository;   // ✅ ADDED
import com.cts.repository.MaintenanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository repo;
    private final FleetClient fleetClient;
    private final AssignmentClient assignmentClient;
    private final TripClient tripClient;

    private final InspectionRepository inspectionRepo;

    public MaintenanceRecord schedule(MaintenanceRecord m) {

        validateVehicle(m.getVehicleId());

        m.setStatus(MaintenanceStatus.SCHEDULED);
        m.setPerformedAt(LocalDateTime.now());

        return repo.save(m);
    }

    public MaintenanceRecord start(MaintenanceRecord m) {

        VehicleDTO vehicle = validateVehicle(m.getVehicleId());

        if (!inspectionRepo.existsByVehicleId(m.getVehicleId())) {
            throw new BadRequestException(
                "Vehicle must undergo inspection before maintenance"
            );
        }

        if (repo.existsByVehicleIdAndStatus(
                m.getVehicleId(),
                MaintenanceStatus.IN_PROGRESS)) {

            throw new BadRequestException(
                "Maintenance already in progress for this vehicle"
            );
        }

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new BadRequestException(
                "Vehicle already under maintenance"
            );
        }

        fleetClient.updateVehicleStatus(
                m.getVehicleId(),
                "UNDER_MAINTENANCE"
        );

        assignmentClient.cancelAssignments(m.getVehicleId());
        tripClient.cancelTrips(m.getVehicleId());

        m.setStatus(MaintenanceStatus.IN_PROGRESS);
        m.setPerformedAt(LocalDateTime.now());

        return repo.save(m);
    }

    public MaintenanceRecord complete(Long id) {

        MaintenanceRecord m = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Maintenance not found"));

        fleetClient.updateVehicleStatus(
                m.getVehicleId(),
                "AVAILABLE"
        );

        m.setStatus(MaintenanceStatus.COMPLETED);

        return repo.save(m);
    }

    public MaintenanceRecord cancel(Long id) {

        MaintenanceRecord m = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Maintenance not found"));

        m.setStatus(MaintenanceStatus.CANCELLED);

        return repo.save(m);
    }

    private VehicleDTO validateVehicle(Long vehicleId) {
        try {
            return fleetClient.getVehicle(vehicleId);
        } catch (Exception e) {
            throw new ResourceNotFoundException(
                    "Vehicle not found with id " + vehicleId
            );
        }
    }

    public List<MaintenanceRecord> getAll() {
        return repo.findAll();
    }

    public MaintenanceRecord getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Maintenance not found"));
    }

    public List<MaintenanceRecord> byVehicle(Long vehicleId) {
        return repo.findByVehicleId(vehicleId);
    }
}