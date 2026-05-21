package com.cts.service;

import com.cts.dto.KPIDTO;
import com.cts.entity.KPI;
import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.KPIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KPIServiceImpl implements KPIService {

    @Autowired
    private KPIRepository kpiRepo;

    @Override
    public KPIDTO createKPI(KPIDTO dto) {
        KPI kpi = new KPI();
        kpi.setName(dto.getName());
        kpi.setDefinition(dto.getDefinition());
        kpi.setTarget(dto.getTarget());
        kpi.setCurrentValue(dto.getCurrentValue());
        kpi.setCategory(dto.getCategory());
        // Auto-set current timestamp as reporting period
        kpi.setReportingPeriod(LocalDateTime.now());
        return mapToDTO(kpiRepo.save(kpi));
    }

    @Override
    public List<KPIDTO> getAllKPIs() {
        return kpiRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<KPIDTO> getKPIsByCategory(String category) {
        List<KPI> kpis = kpiRepo.findByCategory(category);
        if (kpis.isEmpty()) {
            throw new ResourceNotFoundException("No KPIs found for category: " + category);
        }
        return kpis.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public KPIDTO getKPIById(Long id) {
        KPI kpi = kpiRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KPI not found with ID: " + id));
        return mapToDTO(kpi);
    }

    @Override
    public KPIDTO updateKPI(Long id, KPIDTO dto) {
        KPI kpi = kpiRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KPI not found with ID: " + id));

        kpi.setCurrentValue(dto.getCurrentValue());
        kpi.setTarget(dto.getTarget());
        kpi.setReportingPeriod(LocalDateTime.now());

        return mapToDTO(kpiRepo.save(kpi));
    }

    @Override
    public void deleteKPI(Long id) {
        kpiRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KPI not found with ID: " + id));
        kpiRepo.deleteById(id);
    }

    private KPIDTO mapToDTO(KPI kpi) {
        KPIDTO dto = new KPIDTO();
        dto.setKpiId(kpi.getKpiId());
        dto.setName(kpi.getName());
        dto.setDefinition(kpi.getDefinition());
        dto.setTarget(kpi.getTarget());
        dto.setCurrentValue(kpi.getCurrentValue());
        dto.setReportingPeriod(kpi.getReportingPeriod());
        dto.setCategory(kpi.getCategory());
        return dto;
    }
}
