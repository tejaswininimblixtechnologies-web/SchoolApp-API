package com.nimblix.SchoolPEPProject.Request;


import lombok.Getter;
import lombok.Setter;


/**
 * Request DTO used when marking attendance for a student.
 * Contains all necessary details like school, class, section, date, and status.
 */

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
