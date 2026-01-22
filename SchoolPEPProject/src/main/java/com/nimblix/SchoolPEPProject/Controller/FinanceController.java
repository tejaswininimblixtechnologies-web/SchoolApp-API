package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/finance/fees")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/collected/total")
    public ResponseEntity<Map<String, Object>> getTotalFeesCollected(
            @RequestParam Long schoolId) {

        Double totalAmount = financeService.getTotalFeesCollected(schoolId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("schoolId", schoolId);
        response.put("totalCollectedAmount", totalAmount);

        return ResponseEntity.ok(response);
    }
}
