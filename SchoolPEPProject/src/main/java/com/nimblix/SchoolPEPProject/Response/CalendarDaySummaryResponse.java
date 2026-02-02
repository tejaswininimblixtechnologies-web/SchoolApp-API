package com.nimblix.SchoolPEPProject.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarDaySummaryResponse {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String date;

    private String dayOfWeek;

    private Boolean hasDiaryEntry;

    private Boolean hasEvents;

    private Boolean hasMeetings;

    private Boolean hasClasses;

    private Boolean hasQuickNotes;

    // Additional counts for more detailed view
    private Long diaryEntryCount;
    private Long eventCount;
    private Long meetingCount;
    private Long classCount;
    private Long quickNoteCount;

    // Day status
    private String dayType; // WEEKDAY, WEEKEND, HOLIDAY
    private Boolean isToday;
}
