package com.cts.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {

    private String serviceName;
    private String action;
    private String method;
    private String username;
    private String details;
    private LocalDateTime timestamp;
    
}