package com.cts.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cts.dto.AuditLogDTO;
import com.cts.entity.AuditLog;
import com.cts.repository.AuditRepository;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public void save(AuditLogDTO dto) {
        AuditLog log = new AuditLog();
        log.setServiceName(dto.getServiceName());
        log.setAction(dto.getAction());
        log.setMethod(dto.getMethod());
        log.setUsername(dto.getUsername());
        log.setDetails(dto.getDetails());
        log.setTimestamp(LocalDateTime.now());
        auditRepository.save(log);
    }

    @Override
    public List<AuditLog> getAll() {
        return auditRepository.findAll();
    }

    @Override
    public List<AuditLog> getByUsername(String username) {
        return auditRepository.findByUsername(username);
    }
}
