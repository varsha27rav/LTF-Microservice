package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.DeliveryRequest;
import com.cts.dto.ProofRequest;
import com.cts.entity.DeliveryRecord;
import com.cts.entity.ProofOfReceipt;
import com.cts.service.DeliveryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService service;

    @PostMapping
    public String createDelivery(@Valid @RequestBody DeliveryRequest request) {
        return service.createDelivery(request);
    }

    @GetMapping
    public List<DeliveryRecord> getAllDeliveries() {
        return service.getAllDeliveries();
    }

    @GetMapping("/{deliveryId}")
    public DeliveryRecord getDeliveryById(@PathVariable Long deliveryId) {
        return service.getDeliveryById(deliveryId);
    }

    @GetMapping("/order/{orderId}")
    public DeliveryRecord getDeliveryByOrderId(@PathVariable Long orderId) {
        return service.getDeliveryByOrderId(orderId);
    }

    @PostMapping("/proof")
    public String uploadProof(@Valid @RequestBody ProofRequest request) {
        return service.uploadProof(request);
    }

    @GetMapping("/proof/{deliveryId}")
    public List<ProofOfReceipt> getProofByDeliveryId(@PathVariable Long deliveryId) {
        return service.getProofByDeliveryId(deliveryId);
    }

    @PutMapping("/{deliveryId}/close")
    public String closeDelivery(@PathVariable Long deliveryId) {
        return service.closeDelivery(deliveryId);
    }
}