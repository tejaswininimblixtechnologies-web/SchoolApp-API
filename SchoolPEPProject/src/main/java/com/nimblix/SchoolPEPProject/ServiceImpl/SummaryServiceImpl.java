package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Response.TeacherDiarySummaryResponse;
import com.nimblix.SchoolPEPProject.Service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SummaryServiceImpl implements SummaryService {

    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<TeacherDiarySummaryResponse> getTeacherDiarySummary(YearMonth yearMonth) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Getting teacher diary summary for teacher: {} for month: {}", teacher.getId(), yearMonth);

            // For now, return sample data for testing
            LocalDate monthStart = yearMonth.atDay(1);
            LocalDate monthEnd = yearMonth.atEndOfMonth();
            long workingDays = calculateWorkingDays(monthStart, monthEnd);

            TeacherDiarySummaryResponse summary = TeacherDiarySummaryResponse.builder()
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .month(yearMonth)
                    .summaryDate(LocalDate.now())
                    .totalDiaryEntries(22L) // Sample data
                    .totalClassesConducted(88L) // Sample data
                    .totalMeetings(5L) // Sample data
                    .totalEvents(3L) // Sample data
                    .totalQuickNotes(15L) // Sample data
                    .workingDays(workingDays)
                    .averageDiaryEntriesPerDay(workingDays > 0 ? 22.0 / workingDays : 0.0)
                    .averageClassesPerDay(workingDays > 0 ? 88.0 / workingDays : 0.0)
                    .monthStartDate(monthStart.toString())
                    .monthEndDate(monthEnd.toString())
                    .build();

            log.info("Teacher diary summary retrieved successfully (test mode)");
            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            log.error("Error getting teacher diary summary: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<TeacherDiarySummaryResponse> getTeacherDiarySummaryByYearMonth(Integer year, Integer month) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            return getTeacherDiarySummary(yearMonth);
        } catch (Exception e) {
            log.error("Error getting teacher diary summary by year/month: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<TeacherDiarySummaryResponse> getCurrentMonthSummary() {
        try {
            YearMonth currentMonth = YearMonth.now();
            return getTeacherDiarySummary(currentMonth);
        } catch (Exception e) {
            log.error("Error getting current month summary: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<TeacherDiarySummaryResponse> getTodaySummary() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            log.info("Getting today's summary for teacher: {}", teacher.getId());

            // For today's summary, show current month data
            YearMonth currentMonth = YearMonth.now();
            LocalDate monthStart = currentMonth.atDay(1);
            LocalDate monthEnd = currentMonth.atEndOfMonth();
            long workingDays = calculateWorkingDays(monthStart, monthEnd);

            TeacherDiarySummaryResponse summary = TeacherDiarySummaryResponse.builder()
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .month(currentMonth)
                    .summaryDate(today)
                    .totalDiaryEntries(22L)
                    .totalClassesConducted(88L)
                    .totalMeetings(5L)
                    .totalEvents(3L)
                    .totalQuickNotes(15L)
                    .workingDays(workingDays)
                    .averageDiaryEntriesPerDay(workingDays > 0 ? 22.0 / workingDays : 0.0)
                    .averageClassesPerDay(workingDays > 0 ? 88.0 / workingDays : 0.0)
                    .monthStartDate(monthStart.toString())
                    .monthEndDate(monthEnd.toString())
                    .build();

            log.info("Today's summary retrieved successfully (test mode)");
            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            log.error("Error getting today's summary: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Helper method to calculate working days (excluding weekends)
    private long calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        long workingDays = 0;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            if (current.getDayOfWeek().getValue() <= 5) { // Monday to Friday
                workingDays++;
            }
            current = current.plusDays(1);
        }

        return workingDays;
    }

    // Helper method
    private Teacher getAuthenticatedTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        return teacherRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }
}
