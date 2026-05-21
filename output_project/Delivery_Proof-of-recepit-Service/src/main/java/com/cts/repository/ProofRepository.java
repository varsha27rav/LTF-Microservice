package com.cts.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.entity.ProofOfReceipt;

public interface ProofRepository extends JpaRepository<ProofOfReceipt, Long> {
    List<ProofOfReceipt> findByDeliveryId(Long deliveryId);
}
