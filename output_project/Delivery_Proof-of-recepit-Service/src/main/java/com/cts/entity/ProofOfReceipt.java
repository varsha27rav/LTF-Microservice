package com.cts.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proof_of_receipt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProofOfReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proofId;

    private Long deliveryId;
    private Long customerId;

    private String receivedAt;
    private String fileURI;
    private String status;
}