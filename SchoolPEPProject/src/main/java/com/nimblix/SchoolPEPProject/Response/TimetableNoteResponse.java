package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TimetableNoteResponse {

    private Long id;
    private String noteTitle;
    private String noteDescription;
    private String uploadedBy;
    private String uploadedDate;
    private String subject;
    private String dayOfWeek;
    private Integer periodNumber;
}
