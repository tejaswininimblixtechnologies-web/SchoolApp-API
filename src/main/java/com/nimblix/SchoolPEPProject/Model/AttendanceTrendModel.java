package com.nimblix.SchoolPEPProject.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceTrendModel {

    private String date;
    private Long totalStudents;
    private Long presentCount;
    private Long absentCount;
    private Double attendancePercentage;
}
