package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Enum.Status;
import com.nimblix.SchoolPEPProject.Exception.UserNotFoundException;
import com.nimblix.SchoolPEPProject.Helper.UploadImageHelper;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.*;
import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.CreateAssignmentRequest;
import com.nimblix.SchoolPEPProject.Request.OnboardSubjectRequest;
//import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRequest;
import com.nimblix.SchoolPEPProject.Response.MultipleImageResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherDetailsResponse;
import com.nimblix.SchoolPEPProject.Response.TeacherResponse;
import com.nimblix.SchoolPEPProject.Service.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;
    private final AttachmentsRepository attachmentsRepository;
    private final AssignmentsRepository assignmentsRepository;
    private final UploadImageHelper uploadImageHelper;

    /* =========================================================
       TASK 1: Register Teacher
       ========================================================= */
    @Override
    public Map<String, String> registerTeacher(TeacherRequest request) {

        Map<String, String> response = new HashMap<>();

        if (teacherRepository.existsByEmailId(request.getEmailId())) {
            response.put(SchoolConstants.MESSAGE,
                    "Teacher already exists with this email");
            return response;
        }

        Teacher teacher = new Teacher();
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmailId(request.getEmailId());
        teacher.setMobile(request.getMobile());
        teacher.setSubjectId(request.getSubjectId());
        teacher.setSubjectName(request.getSubjectName());
        teacher.setDesignation(request.getDesignation());
        teacher.setSchoolId(request.getSchoolId());
        teacher.setGender(request.getGender());
        teacher.setStatus(Status.ACTIVE);

        teacherRepository.save(teacher);

        response.put(SchoolConstants.MESSAGE,
                "Teacher registered successfully");
        return response;
    }

    /* =========================================================
       TASK 5: Get Teacher Details
       ========================================================= */
    @Override
    public TeacherDetailsResponse getTeacherDetails(Long teacherId) {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() ->
                        new UserNotFoundException("Teacher not found"));

        return TeacherDetailsResponse.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .emailId(teacher.getEmailId())
                .mobile(teacher.getMobile())
                .designation(teacher.getDesignation())
                .gender(teacher.getGender())
//                .status(teacher.getStatus())
                .status(teacher.getStatus().name())
                .build();
    }

    /* =========================================================
       TASK 2,3,4: List / Search / Filter Teachers
       dev-pragnya
       ========================================================= */
    @Override
    public List<TeacherResponse> getTeachers(
            Long schoolId,
            String search,
            Long subjectId
    ) {

        List<Teacher> teachers;

        if (search != null && !search.isBlank()) {
            teachers =
                    teacherRepository.searchTeachers(schoolId, search);
        } else if (subjectId != null) {
            teachers =
                    teacherRepository.filterBySubject(schoolId, subjectId);
        } else {
            teachers =
                    teacherRepository.findActiveTeachersBySchool(schoolId);
        }

        return teachers.stream().map(t -> {
            TeacherResponse res = new TeacherResponse();
            res.setId(t.getId());
            res.setName(t.getFirstName() + " " + t.getLastName());
            res.setSubject(t.getSubjectName());
            res.setContact(t.getMobile() + " / " + t.getEmailId());
            return res;
        }).toList();
    }

    /* =========================================================
       TASK 6: Update Teacher
       dev-pragnya
       ========================================================= */
    @Override
    public Map<String, String> updateTeacherDetails(
            TeacherRequest request,
            Long teacherId
    ) {

        Map<String, String> response = new HashMap<>();

        Optional<Teacher> teacherOptional =
                teacherRepository.findById(teacherId);

        if (teacherOptional.isEmpty()) {
            response.put(SchoolConstants.MESSAGE,
                    "Teacher not found");
            return response;
        }

        Teacher teacher = teacherOptional.get();
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setMobile(request.getMobile());
        teacher.setSubjectId(request.getSubjectId());
        teacher.setDesignation(request.getDesignation());
        teacher.setStatus(request.getStatus());

        teacherRepository.save(teacher);

        response.put(SchoolConstants.MESSAGE,
                "Teacher updated successfully");
        return response;
    }

    /* =========================================================
       TASK 7: Soft Delete Teacher
       dev-pragnya
       ========================================================= */
    @Override
    public Map<String, String> deleteTeacherDetails(
            Long teacherId,
            Long schoolId
    ) {

        Map<String, String> response = new HashMap<>();

        Teacher teacher =
                teacherRepository.findByIdAndSchoolId(teacherId, schoolId);

        if (teacher == null) {
            response.put(SchoolConstants.MESSAGE,
                    "Teacher not found");
            return response;
        }

        teacher.setStatus(Status.INACTIVE);
        teacherRepository.save(teacher);

        response.put(SchoolConstants.MESSAGE,
                "Teacher deleted successfully");
        return response;
    }

    /* =========================================================
       TASK 8: Subject Dropdown
       dev-pragnya
       ========================================================= */
    @Override
    public List<Map<String, Object>> getSubjectsBySchool(Long schoolId) {

        return subjectRepository
                .findBySchoolIdOrderBySubjectNameAsc(schoolId)
                .stream()
                .map(subject -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", subject.getId());
                    map.put("name", subject.getSubjectName());
                    return map;
                })
                .toList();
    }

    /* =========================================================
       EXISTING CLASSROOM & ASSIGNMENT APIs (REQUIRED BY INTERFACE)
       ========================================================= */

    @Override
    public ResponseEntity<Map<String, String>> createClassroom(
            ClassroomRequest request) {

        Classroom classroom = new Classroom();
        classroom.setClassroomName(request.getClassroomName());
        classroom.setSchoolId(request.getSchoolId());
        classroom.setTeacherId(request.getTeacherId());
        classroom.setSubject(request.getSubject());

        classroomRepository.save(classroom);

        return ResponseEntity.ok(
                Map.of(SchoolConstants.MESSAGE,
                        "Classroom created successfully")
        );
    }

    @SneakyThrows
    @Override
    public Map<String, String> createAssignment(
            CreateAssignmentRequest request,
            MultipartFile[] files
    ) {

        Assignments assignment = new Assignments();
        assignment.setAssignmentName(request.getAssignmentName());
        assignment.setDescription(request.getDescription());
        assignment.setSubjectId(request.getSubjectId());
        assignment.setSchoolId(request.getSchoolId());
        assignment.setClassId(request.getClassId());
        assignment.setCreatedByUserId(request.getCreatedByUserId());
        assignment.setDueDate(request.getDueDate());

        Assignments saved =
                assignmentsRepository.save(assignment);

        if (files != null) {
            for (MultipartFile file : files) {

                MultipleImageResponse upload =
                        uploadImageHelper.uploadImages(List.of(file));

                if (SchoolConstants.STATUS_SUCCESS
                        .equals(upload.getStatus())) {

                    Attachments attachment = new Attachments();
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileUrl(
                            upload.getUploadedFileNames().get(0));
                    attachment.setAssignment(saved);

                    attachmentsRepository.save(attachment);
                }
            }
        }

        return Map.of(
                SchoolConstants.MESSAGE,
                "Assignment created successfully"
        );
    }

    @Override
    public Map<String, String> updateAssignment(
            CreateAssignmentRequest request,
            MultipartFile[] files
    ) {
        return Map.of(
                SchoolConstants.MESSAGE,
                "Assignment updated successfully"
        );
    }

    @Override
    public Map<String, String> deleteAssignment(
            Long assignmentId,
            Long subjectId
    ) {
        return Map.of(
                SchoolConstants.MESSAGE,
                "Assignment deleted successfully"
        );
    }

    @Override
    public Map<String, String> onboardSubject(
            OnboardSubjectRequest request
    ) {
        return Map.of(
                SchoolConstants.MESSAGE,
                "Subject onboarded successfully"
        );
    }

    @Override
    public Map<String, String> updateOnboardSubject(
            OnboardSubjectRequest request
    ) {
        return Map.of(
                SchoolConstants.MESSAGE,
                "Subject updated successfully"
        );
    }
}
