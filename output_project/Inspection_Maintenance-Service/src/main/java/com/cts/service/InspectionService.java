package com.cts.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.cts.constants.InspectionStatus;
import com.cts.constants.Role;
import com.cts.constants.VehicleStatus; // ✅ ADDED
import com.cts.dto.UserDTO;
import com.cts.dto.VehicleDTO; // ✅ ADDED
import com.cts.entity.InspectionRecord;
import com.cts.exception.BadRequestException;
import com.cts.exception.ResourceNotFoundException;
import com.cts.exception.ServiceUnavailableException;
import com.cts.repository.InspectionRepository;

import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class InspectionService {

	private final InspectionRepository repo;
	private final FleetClient fleetClient;
	private final UserClient userClient;

	// ✅ CREATE INSPECTION
	public InspectionRecord create(@Valid InspectionRecord record) {

		// ✅ 1️⃣ Validate vehicle exists + FETCH DATA
		VehicleDTO vehicle;
		try {
			vehicle = fleetClient.getVehicle(record.getVehicleId());
		} catch (Exception e) {
			throw new ResourceNotFoundException("Vehicle not found with id " + record.getVehicleId());
		}

		// ✅ ✅ ✅ NEW LOGIC (ONLY ADDITION)
		// ✅ Ensure vehicle is AVAILABLE for inspection
		if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
			throw new BadRequestException("Vehicle is not available for inspection");
		}

		// ✅ 2️⃣ Validate inspector exists
		UserDTO user;
		try {
		    user = userClient.getUser(record.getInspectorId());
		} catch (Exception e) {
		    e.printStackTrace();  // ✅ ADD THIS
		    throw new ResourceNotFoundException("Inspector not found...");
		}

		// ✅ 3️⃣ Validate role
		if (user.getRole() != Role.FLEET_MANAGER) {
			throw new BadRequestException("Only Fleet Manager can perform vehicle inspection");
		}

		// ✅ 4️⃣ Save inspection
		record.setPerformedAt(LocalDateTime.now());
		record.setStatus(InspectionStatus.COMPLETED);

		return repo.save(record);
	}

	// ✅ UPDATE INSPECTION
	public InspectionRecord update(Long id, @Valid InspectionRecord record) {

		InspectionRecord existing = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Inspection not found"));

		// ✅ Validate vehicle
		VehicleDTO vehicle;
		try {
			vehicle = fleetClient.getVehicle(existing.getVehicleId());
		} catch (Exception e) {
			throw new ResourceNotFoundException("Vehicle not found");
		}

		// ✅ ✅ OPTIONAL (same rule for update)
		if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
			throw new BadRequestException("Vehicle is not available for inspection update");
		}

		// ✅ Validate inspector
		UserDTO user;
		try {
			user = userClient.getUser(record.getInspectorId());
		} catch (Exception e) {
			throw new ResourceNotFoundException("Inspector not found");
		}

		if (user.getRole() != Role.FLEET_MANAGER) {
			throw new BadRequestException("Only Fleet Manager can update inspection");
		}

		existing.setConditionRating(record.getConditionRating());
		existing.setFindings(record.getFindings());
		existing.setPhotoURI(record.getPhotoURI());

		return repo.save(existing);
	}

	// ✅ EXISTING METHODS (UNCHANGED)

	private void validateVehicle(Long vehicleId) {
		try {
			fleetClient.getVehicle(vehicleId);
		} catch (FeignException.NotFound e) {
			throw new ResourceNotFoundException("Vehicle not found");
		} catch (FeignException e) {
			throw new ServiceUnavailableException("Fleet service unavailable");
		}
	}

	private void validateInspector(Long inspectorId) {
		UserDTO user;
		try {
			user = userClient.getUser(inspectorId);
		} catch (FeignException.NotFound e) {
			throw new ResourceNotFoundException("Inspector not found");
		} catch (FeignException e) {
			throw new ServiceUnavailableException("User service unavailable");
		}

		if (user.getRole() != Role.FLEET_MANAGER) {
			throw new BadRequestException("User is not authorized to inspect vehicle");
		}
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}

	public List<InspectionRecord> getAll() {
		return repo.findAll();
	}

	public InspectionRecord getById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inspection not found"));
	}

	public List<InspectionRecord> byVehicle(Long vehicleId) {
		return repo.findByVehicleId(vehicleId);
	}
}