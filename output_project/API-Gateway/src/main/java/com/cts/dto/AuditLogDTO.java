package com.cts.dto;

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
}