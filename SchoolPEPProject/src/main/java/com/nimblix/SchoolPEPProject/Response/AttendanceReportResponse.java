package com.nimblix.SchoolPEPProject.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/**
 * Response DTO representing attendance statistics for a specific date.
 * Used to return summarized attendance data in reports or dashboards.
 */
// Marks attendance for a student

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceReportResponse {

    private String date;
    private Long presentCount;
    private Long absentCount;
    private Double attendancePercentage;

}
