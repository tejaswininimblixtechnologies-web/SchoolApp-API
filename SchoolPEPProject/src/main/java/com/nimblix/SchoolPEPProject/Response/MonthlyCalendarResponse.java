package com.nimblix.SchoolPEPProject.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Data
@Builder
public class MonthlyCalendarResponse {

    private YearMonth yearMonth;
    private String monthName;
    private Integer year;
    private Long totalEvents;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate monthStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate monthEnd;

    private List<CalendarEventResponse> events;
    private List<DailyEventSummary> dailySummaries;

    @Data
    @Builder
    public static class DailyEventSummary {
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        private Long eventCount;
        private List<String> eventTypes;
        private Boolean hasEvents;
    }
}
