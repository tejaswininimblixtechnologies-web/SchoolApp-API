package com.nimblix.SchoolPEPProject.Response;


import lombok.Getter;
import lombok.Setter;


/**
 * Response DTO that holds the overall attendance percentage
 * for a given school or date range.
 */

@Getter
@Setter
public class AttendanceSummaryResponse {
    private Double attendancePercentage;
}
