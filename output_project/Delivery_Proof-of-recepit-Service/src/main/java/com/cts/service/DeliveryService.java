package com.cts.service;

import java.util.List;
import com.cts.dto.*;
import com.cts.entity.DeliveryRecord;
import com.cts.entity.ProofOfReceipt;

public interface DeliveryService {

    String createDelivery(DeliveryRequest request);

    List<DeliveryRecord> getAllDeliveries();

    DeliveryRecord getDeliveryById(Long deliveryId);

    DeliveryRecord getDeliveryByOrderId(Long orderId);

    String uploadProof(ProofRequest request);

    List<ProofOfReceipt> getProofByDeliveryId(Long deliveryId);

    String closeDelivery(Long deliveryId);
    
}
