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


    @GetMapping("/pending/total")
    public ResponseEntity<Double> getPendingFees(
            @RequestParam Long schoolId) {

        return ResponseEntity.ok(
                financeService.getTotalPendingFees(schoolId)
        );
    }


    @GetMapping("/collected/total")
    public ResponseEntity<Double> getCollectedFees(
            @RequestParam Long schoolId) {

        return ResponseEntity.ok(
                financeService.getTotalFeesCollected(schoolId)
        );
    }
}
