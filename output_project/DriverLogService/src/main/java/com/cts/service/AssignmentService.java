package com.cts.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cts.constants.AssignmentStatus;
import com.cts.constants.DriverStatus;
import com.cts.constants.VehicleStatus;
import com.cts.dto.VehicleDTO;
import com.cts.entity.Driver;
import com.cts.entity.DriverAssignment;
import com.cts.exception.BadRequestException;
import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.AssignmentRepository;
import com.cts.repository.DriverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final DriverRepository driverRepo;
    private final AssignmentRepository assignmentRepo;
    private final FleetClient fleetClient;

    public DriverAssignment assign(Long driverId, Long vehicleId, Long routeId, String assignedBy) {

        // ✅ Fetch vehicle ONLY ONCE
        VehicleDTO vehicle;
        try {
            vehicle = fleetClient.getVehicle(vehicleId);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + vehicleId);
        }

        if (vehicle == null) {
            throw new ResourceNotFoundException("Vehicle not found");
        }

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new BadRequestException("Vehicle not available");
        }

        // ✅ Driver validation
        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        if (assignmentRepo.existsByDriverIdAndStatus(driverId, AssignmentStatus.ACTIVE)) {
            throw new BadRequestException("Driver already has an active assignment");
        }

        if (assignmentRepo.existsByVehicleIdAndStatus(vehicleId, AssignmentStatus.ACTIVE)) {
            throw new BadRequestException("Vehicle already assigned");
        }

        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            throw new BadRequestException("Driver not available");
        }

        // ✅ Create assignment
        DriverAssignment assignment = new DriverAssignment();
        assignment.setDriverId(driverId);
        assignment.setVehicleId(vehicleId);
        assignment.setRouteId(routeId);
        assignment.setAssignedBy(assignedBy);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setStatus(AssignmentStatus.ACTIVE);

        driver.setStatus(DriverStatus.ASSIGNED);

        driverRepo.save(driver);
        return assignmentRepo.save(assignment);
    }

    // ✅ GET ASSIGNMENT BY ID
    public DriverAssignment get(Long id) {
        return assignmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
    }

    // ✅ GET ALL ASSIGNMENTS
    public List<DriverAssignment> getAll() {
        return assignmentRepo.findAll();
    }

    // ✅ GET BY DRIVER
    public List<DriverAssignment> getByDriver(Long driverId) {
        return assignmentRepo.findByDriverId(driverId);
    }

    // ✅ GET BY VEHICLE
    public List<DriverAssignment> getByVehicle(Long vehicleId) {
        return assignmentRepo.findByVehicleId(vehicleId);
    }

    // ✅ GET ACTIVE ASSIGNMENTS
    public List<DriverAssignment> getActive() {
        return assignmentRepo.findByStatus(AssignmentStatus.ACTIVE);
    }

    // ✅ COMPLETE ASSIGNMENT
    public DriverAssignment complete(Long assignmentId) {

        DriverAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        if (assignment.getStatus() != AssignmentStatus.ACTIVE) {
            throw new BadRequestException("Only active assignments can be completed");
        }

        assignment.setStatus(AssignmentStatus.COMPLETED);

        // ✅ Update driver back to AVAILABLE
        Driver driver = driverRepo.findById(assignment.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        driver.setStatus(DriverStatus.AVAILABLE);

        driverRepo.save(driver);
        return assignmentRepo.save(assignment);
    }

    // ✅ CANCEL ASSIGNMENT
    public DriverAssignment cancel(Long assignmentId) {

        DriverAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        if (assignment.getStatus() != AssignmentStatus.ACTIVE) {
            throw new BadRequestException("Only active assignments can be cancelled");
        }

        assignment.setStatus(AssignmentStatus.CANCELLED);

        // ✅ Optional: free driver again
        Driver driver = driverRepo.findById(assignment.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        driver.setStatus(DriverStatus.AVAILABLE);

        driverRepo.save(driver);
        return assignmentRepo.save(assignment);
    }
    
    public void cancelByVehicle(Long vehicleId) {

        List<DriverAssignment> assignments = assignmentRepo.findAll();

        for (DriverAssignment a : assignments) {
            if (a.getVehicleId().equals(vehicleId)
                    && a.getStatus() == AssignmentStatus.ACTIVE) {

                a.setStatus(AssignmentStatus.CANCELLED);
                assignmentRepo.save(a);
            }
        }
    }
}