package com.cts.service;

import com.cts.dto.AuditLogDTO;
import com.cts.entity.AuditLog;
import java.util.List;

public interface AuditService {
    void save(AuditLogDTO dto);
    List<AuditLog> getAll();
    List<AuditLog> getByUsername(String username);
}
