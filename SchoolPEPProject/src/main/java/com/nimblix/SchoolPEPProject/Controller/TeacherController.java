package com.nimblix.SchoolPEPProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.CreateAssignmentRequest;
import com.nimblix.SchoolPEPProject.Request.OnboardSubjectRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRequest;
import com.nimblix.SchoolPEPProject.Response.TeacherDetailsResponse;
import com.nimblix.SchoolPEPProject.Service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    /* =========================================================
       TASK 1: Register Teacher
       ========================================================= */
    @PostMapping("/teacherRegister")
    public Map<String, String> registerTeacher(
            @RequestBody TeacherRequest request
    ) {
        return teacherService.registerTeacher(request);
    }

    /* =========================================================
       TASK 5: Get Teacher Details
       ========================================================= */
    @GetMapping("/getTeacher")
    public ResponseEntity<TeacherDetailsResponse> getTeacherDetails(
            @RequestParam Long teacherId
    ) {
        return ResponseEntity.ok(
                teacherService.getTeacherDetails(teacherId)
        );
    }

    /* =========================================================
       TASK 6: Update Teacher
       ========================================================= */
    @PutMapping(
            value = "/updateTeacher",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> updateTeacherDetails(
            @RequestBody TeacherRequest request,
            @RequestParam Long teacherId
    ) {
        return teacherService.updateTeacherDetails(request, teacherId);
    }

    /* =========================================================
       TASK 7: Delete Teacher (Soft Delete)
       ========================================================= */
    @DeleteMapping("/delete")
    public Map<String, String> deleteTeacherRecord(
            @RequestParam Long teacherId,
            @RequestParam Long schoolId
    ) {
        return teacherService.deleteTeacherDetails(teacherId, schoolId);
    }

    /* =========================================================
       EXISTING: Create Classroom
       ========================================================= */
    @PostMapping("/createClassroom")
    public ResponseEntity<Map<String, String>> createClassroom(
            @RequestBody ClassroomRequest request
    ) {
        return teacherService.createClassroom(request);
    }

    /* =========================================================
       EXISTING: Create Assignment (Multipart)
       ========================================================= */
    @PostMapping(
            value = "/createAssignment",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> createAssignment(
            @RequestPart String assignmentJson,
            @RequestPart(required = false) MultipartFile[] files
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CreateAssignmentRequest request =
                    objectMapper.readValue(
                            assignmentJson,
                            CreateAssignmentRequest.class
                    );

            return ResponseEntity.ok(
                    teacherService.createAssignment(request, files)
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            SchoolConstants.STATUS,
                            SchoolConstants.STATUS_ERORR,
                            SchoolConstants.MESSAGE,
                            "Invalid assignment payload"
                    )
            );
        }
    }

    /* =========================================================
       EXISTING: Update Assignment
       ========================================================= */
    @PostMapping(
            value = "/updateAssignment",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> updateAssignment(
            @RequestPart String assignmentJson,
            @RequestPart(required = false) MultipartFile[] files
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CreateAssignmentRequest request =
                    objectMapper.readValue(
                            assignmentJson,
                            CreateAssignmentRequest.class
                    );

            return ResponseEntity.ok(
                    teacherService.updateAssignment(request, files)
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            SchoolConstants.STATUS,
                            SchoolConstants.STATUS_ERORR,
                            SchoolConstants.MESSAGE,
                            "Invalid assignment payload"
                    )
            );
        }
    }

    /* =========================================================
       EXISTING: Onboard Subject
       ========================================================= */
    @PostMapping("/onboardSubject")
    public ResponseEntity<Map<String, String>> onboardSubject(
            @RequestBody OnboardSubjectRequest request
    ) {
        return ResponseEntity.ok(
                teacherService.onboardSubject(request)
        );
    }

    /* =========================================================
       EXISTING: Update Onboarded Subject
       ========================================================= */
    @PostMapping("/updateOnboardedSubject")
    public ResponseEntity<Map<String, String>> updateOnboardSubject(
            @RequestBody OnboardSubjectRequest request
    ) {
        return ResponseEntity.ok(
                teacherService.updateOnboardSubject(request)
        );
    }

    /* =========================================================
       EXISTING: Delete Assignment
       ========================================================= */
    @PostMapping("/deleteAssignmentByIdAndSubjectId")
    public ResponseEntity<Map<String, String>> deleteAssignment(
            @RequestParam Long assignmentId,
            @RequestParam Long subjectId
    ) {
        return ResponseEntity.ok(
                teacherService.deleteAssignment(
                        assignmentId,
                        subjectId
                )
        );
    }
}
