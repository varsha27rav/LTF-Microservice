package com.cts.service;

import java.util.List;

import com.cts.dto.ShipmentRequest;
import com.cts.dto.ShipmentResponse;
import com.cts.dto.StatusUpdateRequest;

public interface ShipmentService {

    public ShipmentResponse createShipment(ShipmentRequest request);

    public ShipmentResponse getShipment(Long orderId);

    public List<ShipmentResponse> getAllShipments();

    public String approveShipment(Long orderId);

    public String updateStatus(Long orderId, StatusUpdateRequest request);

    public String cancelShipment(Long orderId);

    public String trackShipment(Long orderId);

    public List<ShipmentResponse> getShipmentsByCustomer(Long customerId);

    public String getOtp(Long orderId);   // ✅ NEW METHOD
}