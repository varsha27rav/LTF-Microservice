package com.cts.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.entity.DeliveryRecord;

public interface DeliveryRepository extends JpaRepository<DeliveryRecord, Long> {
    Optional<DeliveryRecord> findByOrderId(Long orderId);
}