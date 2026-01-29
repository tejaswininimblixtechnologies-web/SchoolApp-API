package com.nimblix.SchoolPEPProject.Request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkAttendanceRequest {
    private Long schoolId;
    private Long classId;
    private String section;
    private String date;
    private Long studentId;
    private String status;

}
