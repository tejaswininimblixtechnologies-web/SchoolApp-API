package com.nimblix.SchoolPEPProject.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSummaryModel {

    private double averageAttendancePercentage;
    private long totalPresent;
    private long totalAbsent;
    private long workingDaysCount;
}
