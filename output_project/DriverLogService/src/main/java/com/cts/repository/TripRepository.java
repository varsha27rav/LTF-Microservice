package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.constants.TripStatus;
import com.cts.entity.TripLog;

public interface TripRepository extends JpaRepository<TripLog, Long> {

    List<TripLog> findByDriverId(Long driverId);

    List<TripLog> findByVehicleId(Long vehicleId);

    List<TripLog> findByStatus(TripStatus status);
    
    boolean existsByDriverIdAndStatus(Long driverId, TripStatus status);

    boolean existsByVehicleIdAndStatus(Long vehicleId, TripStatus status);
}
