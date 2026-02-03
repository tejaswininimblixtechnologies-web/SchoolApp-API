package com.nimblix.SchoolPEPProject.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TeacherAssignedClassesResponse {

    private Long id;
    private String teacherId;
    private String teacherName;
    private List<AssignedClass> assignedClasses;

    @Getter
    @Setter
    @Builder
    public static class AssignedClass {
        private Long classRoomId;
        private String className;
        private List<AssignedSubject> subjects;
    }

    @Getter
    @Setter
    @Builder
    public static class AssignedSubject {
        private Long subjectId;
        private String subjectName;
        private String subjectCode;
        private String subDescription;
        private Long totalMarks;
    }
}
