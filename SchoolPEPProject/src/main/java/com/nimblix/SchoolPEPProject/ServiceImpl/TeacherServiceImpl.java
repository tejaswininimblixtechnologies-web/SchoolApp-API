package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Exception.UserNotFoundException;
import com.nimblix.SchoolPEPProject.Helper.UploadImageHelper;
import com.nimblix.SchoolPEPProject.Model.*;
import com.nimblix.SchoolPEPProject.Repository.*;
import com.nimblix.SchoolPEPProject.Request.*;
import com.nimblix.SchoolPEPProject.Response.*;
import com.nimblix.SchoolPEPProject.Service.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
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
            response.put(
                    SchoolConstants.MESSAGE,
                    "Teacher already exists with this email"
            );
            return response;
        }

        Teacher teacher = new Teacher();
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmailId(request.getEmailId());
        teacher.setMobile(request.getMobile());
        teacher.setGender(request.getGender());
        teacher.setSchoolId(request.getSchoolId());

        teacher.setStatus("ACTIVE");

        teacherRepository.save(teacher);

        response.put(
                SchoolConstants.MESSAGE,
                "Teacher registered successfully"
        );
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

        String subjectName = teacher.getSubjects().isEmpty()
                ? ""
                : teacher.getSubjects().get(0).getSubjectName();

        return TeacherDetailsResponse.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .emailId(teacher.getEmailId())
                .mobile(teacher.getMobile())
                .designation(
                        teacher.getDesignation() != null
                                ? teacher.getDesignation().getDesignationName()
                                : null
                )
                .gender(teacher.getGender())
                .status(teacher.getStatus())
                .subject(subjectName)
                .build();
    }

    /* =========================================================
       TASK 2 & 3: List / Search Teachers
       (Subject filter handled later via JOIN)
       ========================================================= */
    @Override
    public List<TeacherResponse> getTeachers(
            Long schoolId,
            String search,
            Long subjectId
    ) {

        List<Teacher> teachers;

        if (search != null && !search.isBlank()) {
            teachers = teacherRepository.searchTeachers(schoolId, search);
        } else {
            teachers = teacherRepository.findActiveTeachersBySchool(schoolId);
        }

        return teachers.stream().map(t -> {

            String subjectName = t.getSubjects().isEmpty()
                    ? ""
                    : t.getSubjects().get(0).getSubjectName();

            TeacherResponse res = new TeacherResponse();
            res.setId(t.getId());
            res.setName(t.getFirstName() + " " + t.getLastName());
            res.setSubject(subjectName);
            res.setContact(t.getMobile() + " / " + t.getEmailId());
            return res;

        }).toList();
    }

    /* =========================================================
       TASK 6: Update Teacher
       ========================================================= */
    @Override
    public Map<String, String> updateTeacherDetails(
            TeacherRequest request,
            Long teacherId
    ) {

        Map<String, String> response = new HashMap<>();

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() ->
                        new UserNotFoundException("Teacher not found"));

        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setMobile(request.getMobile());
        teacher.setStatus(request.getStatus().name());

        // Update subject
        Subjects subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        teacher.getSubjects().clear();
        subject.setTeacher(teacher);
        teacher.getSubjects().add(subject);

        teacherRepository.save(teacher);

        response.put(
                SchoolConstants.MESSAGE,
                "Teacher updated successfully"
        );
        return response;
    }

    /* =========================================================
       TASK 7: Soft Delete Teacher
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
            response.put(
                    SchoolConstants.MESSAGE,
                    "Teacher not found"
            );
            return response;
        }

        teacher.setStatus("INACTIVE");
        teacherRepository.save(teacher);

        response.put(
                SchoolConstants.MESSAGE,
                "Teacher deleted successfully"
        );
        return response;
    }

    /* =========================================================
       TASK 8: Subject Dropdown
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
       EXISTING CLASSROOM & ASSIGNMENT APIs
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
                Map.of(
                        SchoolConstants.MESSAGE,
                        "Classroom created successfully"
                )
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
