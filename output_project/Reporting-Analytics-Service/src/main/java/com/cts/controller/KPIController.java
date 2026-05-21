package com.cts.controller;

import com.cts.dto.KPIDTO;
import com.cts.service.KPIServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reporting/kpi")
public class KPIController {

    @Autowired
    private KPIServiceImpl kpiService;

    @PostMapping
    public ResponseEntity<?> createKPI(
            @RequestHeader("X-User-Role") String role,
            @RequestBody KPIDTO dto) {

        if (!role.equals("ADMIN") && !role.equals("MANAGER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN or MANAGER can create KPIs");
        }
        return new ResponseEntity<>(kpiService.createKPI(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllKPIs(
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("ADMIN") && !role.equals("MANAGER") && !role.equals("DISPATCHER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role to view KPIs");
        }
        return ResponseEntity.ok(kpiService.getAllKPIs());
    }

    // Filter KPIs by category (FLEET, DELIVERY, DRIVER, SHIPMENT, MAINTENANCE)
    @GetMapping("/filter")
    public ResponseEntity<?> getKPIsByCategory(
            @RequestHeader("X-User-Role") String role,
            @RequestParam String category) {

        if (!role.equals("ADMIN") && !role.equals("MANAGER") && !role.equals("DISPATCHER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role to filter KPIs");
        }
        return ResponseEntity.ok(kpiService.getKPIsByCategory(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKPIById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN") && !role.equals("MANAGER") && !role.equals("DISPATCHER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role to view KPI");
        }
        return ResponseEntity.ok(kpiService.getKPIById(id));
    }

    // ADMIN or MANAGER can update KPI values
    @PutMapping("/{id}")
    public ResponseEntity<?> updateKPI(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @RequestBody KPIDTO dto) {

        if (!role.equals("ADMIN") && !role.equals("MANAGER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN or MANAGER can update KPIs");
        }
        return ResponseEntity.ok(kpiService.updateKPI(id, dto));
    }

    // Only ADMIN can delete KPIs
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKPI(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete KPIs");
        }
        kpiService.deleteKPI(id);
        return ResponseEntity.ok("KPI deleted successfully");
    }
}
