package com.cts.service;

import com.cts.dto.KPIDTO;
import java.util.List;

public interface KPIService {
    KPIDTO createKPI(KPIDTO dto);
    List<KPIDTO> getAllKPIs();
    List<KPIDTO> getKPIsByCategory(String category);
    KPIDTO getKPIById(Long id);
    KPIDTO updateKPI(Long id, KPIDTO dto);
    void deleteKPI(Long id);
}
