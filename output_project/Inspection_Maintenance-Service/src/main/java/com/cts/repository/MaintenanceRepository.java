package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.constants.MaintenanceStatus;
import com.cts.entity.MaintenanceRecord;

public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord, Long> {

    List<MaintenanceRecord> findByVehicleId(Long vehicleId);

    boolean existsByVehicleIdAndStatus(Long vehicleId, MaintenanceStatus status);
}