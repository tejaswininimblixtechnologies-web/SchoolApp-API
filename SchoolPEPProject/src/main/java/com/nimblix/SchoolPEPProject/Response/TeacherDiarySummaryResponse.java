package com.nimblix.SchoolPEPProject.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Builder
public class TeacherDiarySummaryResponse {

    private Long teacherId;
    private String teacherName;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth month;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate summaryDate;

    // Summary counts
    private Long totalDiaryEntries;
    private Long totalClassesConducted;
    private Long totalMeetings;
    private Long totalEvents;
    private Long totalQuickNotes;

    // Additional summary details
    private Long workingDays; // Number of working days in the month
    private Double averageDiaryEntriesPerDay;
    private Double averageClassesPerDay;

    // Month period
    private String monthStartDate;
    private String monthEndDate;
}
