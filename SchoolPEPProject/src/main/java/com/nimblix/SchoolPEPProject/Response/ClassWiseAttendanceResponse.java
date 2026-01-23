package com.nimblix.SchoolPEPProject.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClassWiseAttendanceResponse {
    private Long classId;
    private String section;
    private long totalStudents;
    private long present;
    private long absent;
    private double attendancePercentage;
}
