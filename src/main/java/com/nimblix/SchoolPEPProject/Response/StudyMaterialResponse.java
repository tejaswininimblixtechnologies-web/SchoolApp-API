package com.nimblix.SchoolPEPProject.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StudyMaterialResponse {

    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private LocalDateTime createdTime;
}