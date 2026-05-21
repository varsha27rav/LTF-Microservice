package com.cts.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.cts.dto.AuditLogDTO;

@FeignClient(name = "Audit-Service", url = "http://localhost:8085")
public interface AuditFeignClient {

    @PostMapping("/audit/internal")
    void sendLog(@RequestBody AuditLogDTO log);
}