package com.cts.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.constants.DriverStatus;
import com.cts.constants.TripStatus;
import com.cts.constants.VehicleStatus;
import com.cts.dto.VehicleDTO;
import com.cts.entity.Driver;
import com.cts.entity.TripLog;
import com.cts.exception.BadRequestException;
import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.DriverRepository;
import com.cts.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepo;
    private final DriverRepository driverRepo;
    
    @Autowired
    private FleetClient fleetClient;

    public TripLog start(Long driverId, Long vehicleId, Long routeId) {
    	
    	VehicleDTO vehicle = null;

    	try {
    	    vehicle = fleetClient.getVehicle(vehicleId);
    	} catch (Exception e) {
    	    // fallback → allow old behavior
    	}

    	if (vehicle != null && ! VehicleStatus.AVAILABLE.equals(vehicle.getStatus())) {
    	    throw new BadRequestException("Vehicle is under maintenance");
    	}

        Driver d = driverRepo.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        if (d.getStatus() != DriverStatus.ASSIGNED) {
            throw new BadRequestException("Driver not assigned");
        }

        // ✅ Prevent duplicate active trip (driver)
        if (tripRepo.existsByDriverIdAndStatus(driverId, TripStatus.IN_PROGRESS)) {
            throw new BadRequestException("Driver already has an active trip");
        }

        // ✅ Prevent duplicate active trip (vehicle)
        if (tripRepo.existsByVehicleIdAndStatus(vehicleId, TripStatus.IN_PROGRESS)) {
            throw new BadRequestException("Vehicle already in active trip");
        }

        TripLog t = new TripLog();
        t.setDriverId(driverId);
        t.setVehicleId(vehicleId);
        t.setRouteId(routeId);
        t.setStartAt(LocalDateTime.now());
        t.setStatus(TripStatus.IN_PROGRESS);

        return tripRepo.save(t);
    }

    public TripLog end(Long tripId, Double distance, String notes) {

        TripLog t = tripRepo.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (t.getStatus() != TripStatus.IN_PROGRESS) {
            throw new BadRequestException("Trip not active");
        }

        t.setEndAt(LocalDateTime.now());
        t.setDistanceKm(distance);
        t.setNotes(notes);
        t.setStatus(TripStatus.COMPLETED);

        return tripRepo.save(t);
    }

    public TripLog get(Long id) {
        return tripRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
    }

    public List<TripLog> getAll() {
        return tripRepo.findAll();
    }

    public List<TripLog> byDriver(Long driverId) {
        return tripRepo.findByDriverId(driverId);
    }

    public List<TripLog> byVehicle(Long vehicleId) {
        return tripRepo.findByVehicleId(vehicleId);
    }

    public List<TripLog> byStatus(TripStatus status) {
        return tripRepo.findByStatus(status);
    }

    public TripLog cancel(Long tripId) {
        TripLog t = get(tripId);
        t.setStatus(TripStatus.CANCELLED);
        return tripRepo.save(t);
    }
    
    public void cancelByVehicle(Long vehicleId) {

        List<TripLog> trips = tripRepo.findAll();

        for (TripLog t : trips) {
            if (t.getVehicleId().equals(vehicleId)
                    && t.getStatus() == TripStatus.IN_PROGRESS) {

                t.setStatus(TripStatus.CANCELLED);
                tripRepo.save(t);
            }
        }
    }
}
