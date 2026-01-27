package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.StudentRegistrationRequest;
import com.nimblix.SchoolPEPProject.Request.UpdateStudentProfileRequest;
import com.nimblix.SchoolPEPProject.Response.StudentDetailsResponse;
import com.nimblix.SchoolPEPProject.Response.StudentContextResponse;
import com.nimblix.SchoolPEPProject.Response.WeeklyTimetableResponse;
import com.nimblix.SchoolPEPProject.Response.TimetableNoteResponse;
import com.nimblix.SchoolPEPProject.Response.AssignmentFilterResponse;
import com.nimblix.SchoolPEPProject.Response.AssignmentSearchResponse;
import com.nimblix.SchoolPEPProject.Response.AssignmentDetailsResponse;
import com.nimblix.SchoolPEPProject.Response.AssignmentSubmissionResponse;
import com.nimblix.SchoolPEPProject.Response.StudentProfileResponse;
import com.nimblix.SchoolPEPProject.Response.DashboardSummaryResponse;
import com.nimblix.SchoolPEPProject.Response.SubjectResponse;
import com.nimblix.SchoolPEPProject.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;


    /*
    In this API we are registering the student. It will help to onboard the student, In this we are storing the  student
    fullName,emailId and password.
     */
    @PostMapping("/register")
    public ResponseEntity<?> studentRegistration(@RequestBody StudentRegistrationRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            studentService.registerStudent(request);

            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
            response.put(SchoolConstants.MESSAGE, "Student Registration Successful!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

 /*
       This API is used to fetch the student details by using the student I'd.
 */

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getStudentsBySchoolId(
            @RequestParam Long schoolId) {

        List<StudentDetailsResponse> students =
                studentService.getStudentsBySchoolId(schoolId);

        Map<String, Object> response = new HashMap<>();
        response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
        response.put(SchoolConstants.MESSAGE, "Students fetched successfully");
        response.put("data", students);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateStudent(
            @RequestParam Long studentId,
            @RequestBody StudentRegistrationRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {
            studentService.updateStudentDetails(studentId, request);
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
            response.put(SchoolConstants.MESSAGE, "Student updated successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



        @PostMapping("/delete")
        public ResponseEntity<?> deleteStudent(@RequestParam Long studentId) {
            Map<String, Object> response = new HashMap<>();

            try {
                studentService.deleteStudent(studentId);
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Student deleted successfully");
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }



        @PutMapping("/update")
        public ResponseEntity<?> updateStudentPut(
                @RequestParam Long studentId,
                @RequestBody StudentRegistrationRequest request) {

            Map<String, Object> response = new HashMap<>();

            try {
                studentService.updateStudentDetails(studentId, request);
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Student updated successfully (PUT)");
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }



        @DeleteMapping("/delete")
        public ResponseEntity<?> deleteStudentDelete(@RequestParam Long studentId) {
            Map<String, Object> response = new HashMap<>();

            try {
                studentService.deleteStudent(studentId);
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Student deleted successfully (DELETE)");
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        @GetMapping("/context")
        public ResponseEntity<?> getStudentContext() {
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                StudentContextResponse studentContext = studentService.getStudentContext(email);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Student context fetched successfully");
                response.put("data", studentContext);

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        @GetMapping("/timetable/weekly")
        public ResponseEntity<?> getWeeklyTimetable() {
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                List<WeeklyTimetableResponse> timetable = studentService.getWeeklyTimetable(email);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Weekly timetable fetched successfully");
                response.put("data", timetable);

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        @GetMapping("/timetable/notes")
        public ResponseEntity<?> getTimetableNotes(
                @RequestParam(required = false) String subject,
                @RequestParam(required = false) String dayOfWeek,
                @RequestParam(required = false) Integer periodNumber) {
            
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                List<TimetableNoteResponse> notes = studentService.getTimetableNotes(email, subject, dayOfWeek, periodNumber);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Timetable notes fetched successfully");
                response.put("data", notes);

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        // Assignment Management APIs

        @GetMapping("/assignments/filters")
        public ResponseEntity<?> getAssignmentFilters() {
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                AssignmentFilterResponse filters = studentService.getAssignmentFilters(email);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Assignment filters fetched successfully");
                response.put("data", filters);

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        @GetMapping("/assignments/search")
        public ResponseEntity<?> searchAssignments(
                @RequestParam(required = false) String subject,
                @RequestParam(required = false) String status) {
            
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                List<AssignmentSearchResponse> assignments = studentService.searchAssignments(email, subject, status);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Assignments searched successfully");
                response.put("data", assignments);

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        @GetMapping("/assignments/{assignmentId}")
        public ResponseEntity<?> getAssignmentDetails(@PathVariable Long assignmentId) {
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                AssignmentDetailsResponse details = studentService.getAssignmentDetails(email, assignmentId);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Assignment details fetched successfully");
                response.put("data", details);

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        @PostMapping("/assignments/{assignmentId}/upload")
        public ResponseEntity<?> uploadAssignment(
                @PathVariable Long assignmentId,
                @RequestParam("file") MultipartFile file,
                @RequestParam(value = "remarks", required = false) String remarks) {
            
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                ResponseEntity<?> result = studentService.uploadAssignment(email, assignmentId, file, remarks);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, result.getBody());

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        @GetMapping("/assignments/{assignmentId}/status")
        public ResponseEntity<?> getAssignmentSubmissionStatus(@PathVariable Long assignmentId) {
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                AssignmentSubmissionResponse status = studentService.getAssignmentSubmissionStatus(email, assignmentId);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Assignment submission status fetched successfully");
                response.put("data", status);

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        // Student Profile APIs

        @GetMapping("/profile")
        public ResponseEntity<?> getStudentProfile() {
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                StudentProfileResponse profile = studentService.getStudentProfile(email);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Student profile fetched successfully");
                response.put("data", profile);

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        @PutMapping("/profile")
        public ResponseEntity<?> updateStudentProfile(@RequestBody UpdateStudentProfileRequest request) {
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                ResponseEntity<?> result = studentService.updateStudentProfile(email, request);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, result.getBody());

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        // Dashboard Summary API

        @GetMapping("/dashboard/summary")
        public ResponseEntity<?> getDashboardSummary() {
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                DashboardSummaryResponse summary = studentService.getDashboardSummary(email);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Dashboard summary fetched successfully");
                response.put("data", summary);

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        // Supporting APIs

        @GetMapping("/subjects")
        public ResponseEntity<?> getStudentSubjects() {
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                List<SubjectResponse> subjects = studentService.getStudentSubjects(email);

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Student subjects fetched successfully");
                response.put("data", subjects);

                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        @GetMapping("/download/{fileType}")
        public ResponseEntity<?> downloadFile(
                @PathVariable String fileType,
                @RequestParam(required = false) Long assignmentId,
                @RequestParam(required = false) Long noteId) {
            
            Map<String, Object> response = new HashMap<>();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String email = authentication.getName();

                // This is a placeholder implementation
                // In a real implementation, you would:
                // 1. Validate file access permissions
                // 2. Get file path from database based on assignmentId or noteId
                // 3. Check if file exists on server
                // 4. Return file as Resource with proper headers

                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, "File download not implemented yet. Please implement file storage and retrieval logic.");

                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
    }


