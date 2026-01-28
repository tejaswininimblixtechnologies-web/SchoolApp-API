package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherScheduleResponse {
    private String teacherName;
    private String className;
    private String startTime;
    private String endTime;
}
