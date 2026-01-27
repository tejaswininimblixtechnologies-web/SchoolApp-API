package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.ClassesConductedRequest;
import com.nimblix.SchoolPEPProject.Response.ClassesConductedResponse;
import com.nimblix.SchoolPEPProject.Service.ClassesConductedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
@Slf4j
public class ClassesConductedController {

    private final ClassesConductedService classesConductedService;

    // Add Classes Conducted
    @PostMapping("/conducted")
    public ResponseEntity<ClassesConductedResponse> createClass(@Valid @RequestBody ClassesConductedRequest request) {
        log.info("Creating class conducted: {}", request.getClassDate());
        return classesConductedService.createClass(request);
    }

    // Get Classes by Date - for diary view
    @GetMapping("/conducted/date/{classDate}")
    public ResponseEntity<List<ClassesConductedResponse>> getClassesByDate(@PathVariable LocalDate classDate) {
        log.info("Getting classes conducted for date: {}", classDate);
        return classesConductedService.getClassesByDate(classDate);
    }

    // Get Today's Classes - for diary view
    @GetMapping("/conducted/today")
    public ResponseEntity<List<ClassesConductedResponse>> getTodayClasses() {
        log.info("Getting today's classes conducted");
        return classesConductedService.getTodayClasses();
    }

    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        log.info("Classes conducted controller is working!");
        return ResponseEntity.ok("Classes conducted API is working!");
    }
}
