package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AssignmentFilterResponse {

    private List<String> subjects;
    private List<String> statuses;
}
