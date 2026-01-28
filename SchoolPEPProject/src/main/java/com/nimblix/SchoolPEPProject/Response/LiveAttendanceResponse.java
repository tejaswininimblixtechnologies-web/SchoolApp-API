package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveAttendanceResponse {
    private String className;
    private String subject;
    private String time;
    private int present;
    private int total;
    private double percentage;
}
