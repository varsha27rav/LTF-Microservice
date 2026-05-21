package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.constants.DriverStatus;
import com.cts.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByStatus(DriverStatus status);
    
    boolean existsByLicenseNumber(String licenseNumber);

}