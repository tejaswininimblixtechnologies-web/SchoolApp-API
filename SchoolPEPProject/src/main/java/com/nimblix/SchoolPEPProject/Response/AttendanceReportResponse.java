package com.nimblix.SchoolPEPProject.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
