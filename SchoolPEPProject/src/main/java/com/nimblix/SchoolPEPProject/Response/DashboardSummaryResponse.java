package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DashboardSummaryResponse {

    private Integer totalAssignments;
    private Integer pendingAssignments;
    private Integer submittedAssignments;
    private Integer lateAssignments;
    private List<UpcomingDueDate> upcomingDueDates;
    private List<TodayTimetable> todayTimetableSubjects;

    @Getter
    @Setter
    @Builder
    public static class UpcomingDueDate {
        private Long assignmentId;
        private String assignmentTitle;
        private String subject;
        private String dueDate;
        private Integer daysRemaining;
    }

    @Getter
    @Setter
    @Builder
    public static class TodayTimetable {
        private String subject;
        private String teacherName;
        private String startTime;
        private String endTime;
        private String dayOfWeek;
        private Integer periodNumber;
    }
}
