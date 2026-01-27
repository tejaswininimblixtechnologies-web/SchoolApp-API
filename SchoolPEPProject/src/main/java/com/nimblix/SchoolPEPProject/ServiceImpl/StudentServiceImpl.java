package com.nimblix.SchoolPEPProject.ServiceImpl;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Enum.StaffType;
import com.nimblix.SchoolPEPProject.Exception.UserNotFoundException;
import com.nimblix.SchoolPEPProject.Model.Designation;
import com.nimblix.SchoolPEPProject.Model.Role;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Model.Timetable;
import com.nimblix.SchoolPEPProject.Model.TimetableNote;
import com.nimblix.SchoolPEPProject.Model.Assignment;
import com.nimblix.SchoolPEPProject.Model.AssignmentSubmission;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.DesignationRepository;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Repository.TimetableRepository;
import com.nimblix.SchoolPEPProject.Repository.TimetableNoteRepository;
import com.nimblix.SchoolPEPProject.Repository.AssignmentRepository;
import com.nimblix.SchoolPEPProject.Repository.AssignmentSubmissionRepository;
import com.nimblix.SchoolPEPProject.Repository.SubjectListRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentSubjectsRepository;
//import com.nimblix.SchoolPEPProject.Repository.UserRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final DesignationRepository designationRepository;
    private final TimetableRepository timetableRepository;
    private final TimetableNoteRepository timetableNoteRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final SubjectListRepository subjectListRepository;
    private final StudentSubjectsRepository studentSubjectsRepository;

    @Override
    public ResponseEntity<?> registerStudent(StudentRegistrationRequest request) {

        if (!request.getPassword().equals(request.getReEnterPassword())) {
            return ResponseEntity.badRequest()
                    .body("Password and Re-Enter Password do not match!");
        }

        if (studentRepository.existsByEmailId(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Email already registered!");
        }

        // Fetch role (SECURITY) - create if not found
        Role studentRole =
                roleRepository.findByRoleName(SchoolConstants.STUDENT);
        
        if (studentRole == null) {
            // Get the max existing ID and assign next one
            Long maxId = roleRepository.findAll().stream()
                    .mapToLong(Role::getId)
                    .max()
                    .orElse(0L);
            
            studentRole = new Role();
            studentRole.setId(maxId + 1); // Manually assign ID
            studentRole.setRoleName(SchoolConstants.STUDENT);
            studentRole = roleRepository.save(studentRole);
        }

        // Fetch designation (BUSINESS) - create if not found
        Designation studentDesignation =
                designationRepository
                        .findByDesignationName(SchoolConstants.STUDENT)
                        .orElseGet(() -> {
                            Designation newDesignation = new Designation();
                            newDesignation.setDesignationName(SchoolConstants.STUDENT);
                            newDesignation.setStaffType(StaffType.NON_TEACHING);
                            return designationRepository.save(newDesignation);
                        });

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmailId(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setSchoolId(request.getSchoolId());

        student.setStatus(SchoolConstants.ACTIVE);
        student.setIsLogin(Boolean.FALSE);

        // ✅ IMPORTANT FIXES
        student.setStaffType(StaffType.NON_TEACHING); // Students are NOT staff
        student.setDesignation(studentDesignation);
        student.setRole(studentRole);

        Student savedStudent = studentRepository.save(student);

        return ResponseEntity.ok(
                "Student registered successfully with ID: " + savedStudent.getId()
        );
    }

    @Override
    public void updateStudentDetails(Long studentId, StudentRegistrationRequest request) {

        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        if(request.getFirstName()!=null && request.getLastName()!=null){
            existingStudent.setFirstName(request.getFirstName());
            existingStudent.setLastName(request.getLastName());
        }

        if (request.getEmail() != null) {
            existingStudent.setEmailId(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {

            if (!request.getPassword().equals(request.getReEnterPassword())) {
                throw new RuntimeException("Password and Re-Enter Password do not match!");
            }

            existingStudent.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        if (request.getSchoolId() != null) {
            existingStudent.setSchoolId(request.getSchoolId());
        }

        studentRepository.save(existingStudent);
    }

    @Override
    public List<StudentDetailsResponse> getStudentsBySchoolId(Long schoolId) {

        if (schoolId == null || schoolId <= 0) {
            throw new IllegalArgumentException("School ID must be valid");
        }

        List<Student> students = studentRepository.findBySchoolId(schoolId);

        if (students.isEmpty()) {
            throw new UserNotFoundException(
                    "No students found for schoolId: " + schoolId
            );
        }

        return students.stream()
                .map(student -> StudentDetailsResponse.builder()
                        .id(student.getId())
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .emailId(student.getEmailId())
                        .mobile(student.getMobile())
                        .status(student.getStatus())
                        .classId(student.getClassId())
                        .section(student.getSection())
                        .build()
                )
                .toList();
    }


    @Override
    public void deleteStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        // Soft delete
//        student.setStatus(SchoolConstants.IN_ACTIVE);
        studentRepository.delete(student);
//        studentRepository.save(student);
    }

    @Override
    public StudentContextResponse getStudentContext(String email) {
        Student student = studentRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

        return StudentContextResponse.builder()
                .studentId(student.getId())
                .classId(student.getClassId())
                .section(student.getSection())
                .academicYear("2024-2025") // TODO: Get from database or configuration
                .build();
    }

    @Override
    public List<WeeklyTimetableResponse> getWeeklyTimetable(String email) {
        Student student = studentRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

        List<Timetable> timetables = timetableRepository.findByClassSectionAndAcademicYear(
                student.getClassId(),
                student.getSection(),
                "2024-2025" // TODO: Get from student context or configuration
        );

        return timetables.stream()
                .map(timetable -> WeeklyTimetableResponse.builder()
                        .id(timetable.getId())
                        .dayOfWeek(timetable.getDayOfWeek())
                        .periodNumber(timetable.getPeriodNumber())
                        .subject(timetable.getSubject())
                        .teacherName(timetable.getTeacherName())
                        .startTime(timetable.getStartTime())
                        .endTime(timetable.getEndTime())
                        .build())
                .toList();
    }

    @Override
    public List<TimetableNoteResponse> getTimetableNotes(String email, String subject, String dayOfWeek, Integer periodNumber) {
        Student student = studentRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

        List<TimetableNote> notes;

        // If specific parameters provided, filter by them
        if (subject != null && dayOfWeek != null && periodNumber != null) {
            notes = timetableNoteRepository.findByClassSectionSubjectDayAndPeriod(
                    student.getClassId(),
                    student.getSection(),
                    subject,
                    dayOfWeek,
                    periodNumber
            );
        } else if (dayOfWeek != null) {
            notes = timetableNoteRepository.findByClassSectionAndDay(
                    student.getClassId(),
                    student.getSection(),
                    dayOfWeek
            );
        } else {
            notes = timetableNoteRepository.findByClassAndSection(
                    student.getClassId(),
                    student.getSection()
            );
        }

        return notes.stream()
                .map(note -> TimetableNoteResponse.builder()
                        .id(note.getId())
                        .noteTitle(note.getNoteTitle())
                        .noteDescription(note.getNoteDescription())
                        .uploadedBy(note.getUploadedBy())
                        .uploadedDate(note.getUploadedDate())
                        .subject(note.getSubject())
                        .dayOfWeek(note.getDayOfWeek())
                        .periodNumber(note.getPeriodNumber())
                        .build())
                .toList();
    }

    // Assignment Management APIs Implementation

    @Override
    public AssignmentFilterResponse getAssignmentFilters(String email) {
        Student student = studentRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

        // Get distinct subjects for student's class
        List<String> subjects = assignmentRepository.findDistinctSubjectsByClassSectionAndAcademicYear(
                student.getClassId(),
                student.getSection(),
                "2024-2025" // TODO: Get from student context
        );

        // Define assignment statuses
        List<String> statuses = Arrays.asList("PENDING", "SUBMITTED", "LATE", "EVALUATED");

        return AssignmentFilterResponse.builder()
                .subjects(subjects)
                .statuses(statuses)
                .build();
    }

    @Override
    public List<AssignmentSearchResponse> searchAssignments(String email, String subject, String status) {
        Student student = studentRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

        List<Assignment> assignments;

        if (status != null && !status.isEmpty()) {
            assignments = assignmentRepository.findByClassSectionAcademicYearWithStatusFilter(
                    student.getClassId(),
                    student.getSection(),
                    "2024-2025",
                    subject,
                    status,
                    student.getId()
            );
        } else {
            assignments = assignmentRepository.findByClassSectionAcademicYearWithFilters(
                    student.getClassId(),
                    student.getSection(),
                    "2024-2025",
                    subject
            );
        }

        return assignments.stream()
                .map(assignment -> {
                    // Determine submission status
                    String assignmentStatus = "PENDING";
                    Optional<AssignmentSubmission> submission = assignmentSubmissionRepository
                            .findByAssignmentIdAndStudentId(assignment.getId(), student.getId());
                    
                    if (submission.isPresent()) {
                        AssignmentSubmission sub = submission.get();
                        assignmentStatus = sub.getSubmissionStatus();
                        if ("EVALUATED".equals(sub.getEvaluationStatus())) {
                            assignmentStatus = "EVALUATED";
                        }
                    }

                    return AssignmentSearchResponse.builder()
                            .assignmentId(assignment.getId())
                            .subject(assignment.getSubject())
                            .assignmentTitle(assignment.getAssignmentTitle())
                            .assignedDate(assignment.getAssignedDate())
                            .dueDate(assignment.getDueDate())
                            .status(assignmentStatus)
                            .build();
                })
                .toList();
    }

    @Override
    public AssignmentDetailsResponse getAssignmentDetails(String email, Long assignmentId) {
        Student student = studentRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));

        // Verify assignment belongs to student's class
        if (!assignment.getClassId().equals(student.getClassId()) || 
            !assignment.getSection().equals(student.getSection())) {
            throw new RuntimeException("Assignment not accessible to this student");
        }

        return AssignmentDetailsResponse.builder()
                .assignmentId(assignment.getId())
                .subject(assignment.getSubject())
                .assignmentTitle(assignment.getAssignmentTitle())
                .assignmentDescription(assignment.getAssignmentDescription())
                .instructions(assignment.getInstructions())
                .allowedFileTypes(assignment.getAllowedFileTypes())
                .maxFileSize(assignment.getMaxFileSize())
                .assignedDate(assignment.getAssignedDate())
                .dueDate(assignment.getDueDate())
                .createdBy(assignment.getCreatedBy())
                .build();
    }

    @Override
    public ResponseEntity<?> uploadAssignment(String email, Long assignmentId, MultipartFile file, String remarks) {
        try {
            Student student = studentRepository.findByEmailId(email)
                    .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

            Assignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));

            // Verify assignment belongs to student's class
            if (!assignment.getClassId().equals(student.getClassId()) || 
                !assignment.getSection().equals(student.getSection())) {
                return ResponseEntity.badRequest().body("Assignment not accessible to this student");
            }

            // Check if already submitted
            Optional<AssignmentSubmission> existingSubmission = assignmentSubmissionRepository
                    .findByAssignmentIdAndStudentId(assignmentId, student.getId());
            
            if (existingSubmission.isPresent()) {
                return ResponseEntity.badRequest().body("Assignment already submitted");
            }

            // Validate file
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            // Check file size
            if (assignment.getMaxFileSize() != null && file.getSize() > assignment.getMaxFileSize()) {
                return ResponseEntity.badRequest().body("File size exceeds maximum allowed size");
            }

            // Check file type (basic validation)
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid file name");
            }

            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (assignment.getAllowedFileTypes() != null && !assignment.getAllowedFileTypes().isEmpty()) {
                String[] allowedTypes = assignment.getAllowedFileTypes().split(",");
                boolean isAllowed = false;
                for (String type : allowedTypes) {
                    if (type.trim().equalsIgnoreCase(fileExtension)) {
                        isAllowed = true;
                        break;
                    }
                }
                if (!isAllowed) {
                    return ResponseEntity.badRequest().body("File type not allowed. Allowed types: " + assignment.getAllowedFileTypes());
                }
            }

            // Determine submission status
            String submissionStatus = "SUBMITTED";
            LocalDate today = LocalDate.now();
            LocalDate dueDate = LocalDate.parse(assignment.getDueDate());
            
            if (today.isAfter(dueDate)) {
                submissionStatus = "LATE";
            }

            // Save submission (simplified - in real app, save file to storage)
            AssignmentSubmission submission = AssignmentSubmission.builder()
                    .assignment(assignment)
                    .student(student)
                    .fileName(fileName)
                    .fileSize(file.getSize())
                    .filePath("/uploads/assignments/" + fileName) // Simplified path
                    .remarks(remarks)
                    .submissionDate(today.toString())
                    .submissionStatus(submissionStatus)
                    .evaluationStatus("PENDING_EVALUATION")
                    .build();

            assignmentSubmissionRepository.save(submission);

            return ResponseEntity.ok("Assignment submitted successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading assignment: " + e.getMessage());
        }
    }

    @Override
    public AssignmentSubmissionResponse getAssignmentSubmissionStatus(String email, Long assignmentId) {
        Student student = studentRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

        Optional<AssignmentSubmission> submission = assignmentSubmissionRepository
                .findByAssignmentIdAndStudentId(assignmentId, student.getId());

        if (submission.isEmpty()) {
            return AssignmentSubmissionResponse.builder()
                    .assignmentId(assignmentId)
                    .submissionStatus("PENDING")
                    .evaluationStatus("PENDING_EVALUATION")
                    .build();
        }

        AssignmentSubmission sub = submission.get();

        return AssignmentSubmissionResponse.builder()
                .submissionId(sub.getId())
                .assignmentId(assignmentId)
                .submissionStatus(sub.getSubmissionStatus())
                .submissionDate(sub.getSubmissionDate())
                .evaluationStatus(sub.getEvaluationStatus())
                .marksObtained(sub.getMarksObtained())
                .teacherFeedback(sub.getTeacherFeedback())
                .fileName(sub.getFileName())
                .remarks(sub.getRemarks())
                .build();
    }

    // Student Profile APIs Implementation

    @Override
    public StudentProfileResponse getStudentProfile(String email) {
        Student student = studentRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

        // Get subjects from timetable (simplified approach)
        List<String> subjectsEnrolled = assignmentRepository.findDistinctSubjectsByClassSectionAndAcademicYear(
                student.getClassId(),
                student.getSection(),
                "2024-2025" // TODO: Get from student context
        );

        return StudentProfileResponse.builder()
                .studentId(student.getId())
                .profilePhoto(student.getProfilePhoto())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .fullName(student.getFirstName() + " " + student.getLastName())
                .rollNumber(student.getRollNumber())
                .classId(student.getClassId())
                .section(student.getSection())
                .academicYear("2024-2025") // TODO: Get from student context
                .subjectsEnrolled(subjectsEnrolled)
                .emailId(student.getEmailId())
                .mobile(student.getMobile())
                .address(student.getAddress())
                .parentName(student.getParentName())
                .parentContact(student.getParentContact())
                .parentEmail(student.getParentEmail())
                .status(student.getStatus())
                .schoolName("School Name") // TODO: Get from school entity
                .build();
    }

    @Override
    public ResponseEntity<?> updateStudentProfile(String email, UpdateStudentProfileRequest request) {
        try {
            Student student = studentRepository.findByEmailId(email)
                    .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

            // Validate email uniqueness if email is being updated
            if (request.getEmailId() != null && !request.getEmailId().equals(student.getEmailId())) {
                if (studentRepository.existsByEmailId(request.getEmailId())) {
                    return ResponseEntity.badRequest().body("Email already exists");
                }
            }

            // Update only allowed fields
            if (request.getProfilePhoto() != null) {
                student.setProfilePhoto(request.getProfilePhoto());
            }
            if (request.getMobile() != null) {
                student.setMobile(request.getMobile());
            }
            if (request.getEmailId() != null) {
                student.setEmailId(request.getEmailId());
            }
            if (request.getAddress() != null) {
                student.setAddress(request.getAddress());
            }

            studentRepository.save(student);

            return ResponseEntity.ok("Profile updated successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating profile: " + e.getMessage());
        }
    }

    // Dashboard Summary API Implementation

    @Override
    public DashboardSummaryResponse getDashboardSummary(String email) {
        Student student = studentRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

        // Get all assignments for student
        List<Assignment> allAssignments = assignmentRepository.findByClassSectionAcademicYearWithFilters(
                student.getClassId(),
                student.getSection(),
                "2024-2025",
                null
        );

        // Calculate assignment counts
        int totalAssignments = allAssignments.size();
        int pendingAssignments = 0;
        int submittedAssignments = 0;
        int lateAssignments = 0;

        List<DashboardSummaryResponse.UpcomingDueDate> upcomingDueDates = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Assignment assignment : allAssignments) {
            Optional<AssignmentSubmission> submission = assignmentSubmissionRepository
                    .findByAssignmentIdAndStudentId(assignment.getId(), student.getId());

            if (submission.isPresent()) {
                AssignmentSubmission sub = submission.get();
                submittedAssignments++;
                if ("LATE".equals(sub.getSubmissionStatus())) {
                    lateAssignments++;
                }
            } else {
                pendingAssignments++;
            }

            // Calculate upcoming due dates (next 7 days)
            LocalDate dueDate = LocalDate.parse(assignment.getDueDate());
            long daysRemaining = ChronoUnit.DAYS.between(today, dueDate);

            if (daysRemaining >= 0 && daysRemaining <= 7) {
                upcomingDueDates.add(DashboardSummaryResponse.UpcomingDueDate.builder()
                        .assignmentId(assignment.getId())
                        .assignmentTitle(assignment.getAssignmentTitle())
                        .subject(assignment.getSubject())
                        .dueDate(assignment.getDueDate())
                        .daysRemaining((int) daysRemaining)
                        .build());
            }
        }

        // Sort upcoming due dates by days remaining
        upcomingDueDates.sort((a, b) -> Integer.compare(a.getDaysRemaining(), b.getDaysRemaining()));

        // Get today's timetable
        String todayDayOfWeek = today.getDayOfWeek().toString();
        List<Timetable> todayTimetables = timetableRepository.findByClassSectionAcademicYearAndDay(
                student.getClassId(),
                student.getSection(),
                "2024-2025",
                todayDayOfWeek
        );

        List<DashboardSummaryResponse.TodayTimetable> todaySubjects = todayTimetables.stream()
                .map(timetable -> DashboardSummaryResponse.TodayTimetable.builder()
                        .subject(timetable.getSubject())
                        .teacherName(timetable.getTeacherName())
                        .startTime(timetable.getStartTime())
                        .endTime(timetable.getEndTime())
                        .dayOfWeek(timetable.getDayOfWeek())
                        .periodNumber(timetable.getPeriodNumber())
                        .build())
                .toList();

        return DashboardSummaryResponse.builder()
                .totalAssignments(totalAssignments)
                .pendingAssignments(pendingAssignments)
                .submittedAssignments(submittedAssignments)
                .lateAssignments(lateAssignments)
                .upcomingDueDates(upcomingDueDates)
                .todayTimetableSubjects(todaySubjects)
                .build();
    }

    // Supporting APIs Implementation

    @Override
    public List<SubjectResponse> getStudentSubjects(String email) {
        Student student = studentRepository.findByEmailId(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found with email: " + email));

        // Get subjects from assignments for the student's class and section
        List<String> subjectNames = assignmentRepository.findDistinctSubjectsByClassSectionAndAcademicYear(
                student.getClassId(),
                student.getSection(),
                "2024-2025" // TODO: Get from student context
        );

        // Convert to SubjectResponse
        return subjectNames.stream()
                .map(subjectName -> SubjectResponse.builder()
                        .subjectId(null) // We don't have subject ID from assignment query
                        .subjectName(subjectName)
                        .subjectCode("") // Not available from assignment query
                        .description("") // Not available from assignment query
                        .status("ACTIVE") // Default status
                        .build())
                .toList();
    }
}
