package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Request.SchoolRegistrationRequest;
import com.nimblix.SchoolPEPProject.Request.SubscriptionRequest;
import com.nimblix.SchoolPEPProject.Response.SchoolListResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherDiarySummaryResponse;
import org.springframework.http.ResponseEntity;

import java.time.YearMonth;
import java.util.List;

public interface SchoolService {

    School registerSchool(SchoolRegistrationRequest request);

    List<SchoolListResponse> getAllSchools();

    void verifySchoolOtp(String email, String otp);
    
    void validateSubscription(School school);

    School getLoggedInSchool();

    void activatePaidSubscription(School school, SubscriptionRequest request);

    void resendSchoolOtp(String email);

    // ========== SUMMARY METHODS ==========
    
    // Teacher Diary Summary (Month-wise)
    ResponseEntity<TeacherDiarySummaryResponse> getTeacherDiarySummary(YearMonth yearMonth);
    ResponseEntity<TeacherDiarySummaryResponse> getTeacherDiarySummaryByYearMonth(Integer year, Integer month);

    // Current Month Summary
    ResponseEntity<TeacherDiarySummaryResponse> getCurrentMonthSummary();

    // Today's Summary
    ResponseEntity<TeacherDiarySummaryResponse> getTodaySummary();
}
