package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WeeklyTimetableResponse {

    private Long id;
    private String dayOfWeek;
    private Integer periodNumber;
    private String subject;
    private String teacherName;
    private String startTime;
    private String endTime;
}
