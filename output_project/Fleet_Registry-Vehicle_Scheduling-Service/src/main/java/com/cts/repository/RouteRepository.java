package com.cts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
	
	boolean existsByName(String name);
	boolean existsByStopsJson(String stopsJson);
}