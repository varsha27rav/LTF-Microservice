package com.cts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.constants.AssignmentStatus;
import com.cts.entity.DriverAssignment;

public interface AssignmentRepository extends JpaRepository<DriverAssignment, Long> {

    List<DriverAssignment> findByDriverId(Long driverId);

    List<DriverAssignment> findByVehicleId(Long vehicleId);

    List<DriverAssignment> findByStatus(AssignmentStatus status);

    Optional<DriverAssignment> findByDriverIdAndStatus(Long driverId, AssignmentStatus status);

    boolean existsByDriverIdAndStatus(Long driverId, AssignmentStatus status);

    boolean existsByVehicleIdAndStatus(Long vehicleId, AssignmentStatus status);
}