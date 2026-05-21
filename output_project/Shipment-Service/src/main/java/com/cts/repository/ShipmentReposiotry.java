package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entity.Shipment;

public interface ShipmentReposiotry extends JpaRepository<Shipment, Long>{
	List<Shipment> findByCustomerId(Long customerId);
}
