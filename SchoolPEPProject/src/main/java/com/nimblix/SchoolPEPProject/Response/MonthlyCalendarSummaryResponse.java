package com.nimblix.SchoolPEPProject.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;
import java.util.List;

@Data
@Builder
public class MonthlyCalendarSummaryResponse {

    private Long teacherId;
    private String teacherName;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth month;

    private String monthName;
    private Integer year;

    private List<CalendarDaySummaryResponse> days;

    // Summary statistics
    private Long totalDaysWithDiaryEntries;
    private Long totalDaysWithEvents;
    private Long totalDaysWithMeetings;
    private Long totalDaysWithClasses;
    private Long totalDaysWithQuickNotes;

    // Activity counts
    private Long totalDiaryEntries;
    private Long totalEvents;
    private Long totalMeetings;
    private Long totalClasses;
    private Long totalQuickNotes;

    // Working days info
    private Long workingDays;
    private Long weekendDays;
}
