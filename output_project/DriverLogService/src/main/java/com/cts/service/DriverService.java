package com.cts.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cts.constants.AssignmentStatus;
import com.cts.constants.DriverStatus;
import com.cts.entity.Driver;
import com.cts.entity.DriverAssignment;
import com.cts.entity.TripLog;
import com.cts.exception.BadRequestException;
import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.AssignmentRepository;
import com.cts.repository.DriverRepository;
import com.cts.repository.TripRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepo;
    private final TripRepository tripRepo;
    private final AssignmentRepository assignmentRepo;

    public Driver create(@Valid Driver driver) {

        if (driverRepo.existsByLicenseNumber(driver.getLicenseNumber())) {
            throw new BadRequestException("License already exists");
        }

        driver.setStatus(DriverStatus.AVAILABLE);
        return driverRepo.save(driver);
    }


    public Driver get(Long id) {
        return driverRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }

    public List<Driver> getAll() {
        return driverRepo.findAll();
    }

    public List<Driver> getByStatus(DriverStatus status) {
        return driverRepo.findByStatus(status);
    }

    public Driver update(Long id, Driver updated) {
        Driver driver = get(id);

        driver.setName(updated.getName());
        driver.setLicenseNumber(updated.getLicenseNumber());
        driver.setContactInfo(updated.getContactInfo());

        return driverRepo.save(driver);
    }

    public void delete(Long id) {
        Driver driver = get(id);
        driver.setStatus(DriverStatus.INACTIVE);
        driverRepo.save(driver);
    }

    public Driver changeStatus(Long id, DriverStatus status) {
        Driver driver = get(id);
        driver.setStatus(status);
        return driverRepo.save(driver);
    }

    public boolean isAvailable(Long driverId) {
        Driver d = get(driverId);
        return d.getStatus() == DriverStatus.AVAILABLE;
    }

    public List<TripLog> getDriverTrips(Long driverId) {
        return tripRepo.findByDriverId(driverId);
    }

    public double getDutyHours(Long driverId) {
        List<TripLog> trips = tripRepo.findByDriverId(driverId);

        return trips.stream()
                .filter(t -> t.getEndAt() != null)
                .mapToDouble(t ->
                        Duration.between(t.getStartAt(), t.getEndAt()).toHours()
                ).sum();
    }

    public Optional<DriverAssignment> getActiveAssignment(Long driverId) {
        return assignmentRepo.findByDriverIdAndStatus(driverId, AssignmentStatus.ACTIVE);
    }
}