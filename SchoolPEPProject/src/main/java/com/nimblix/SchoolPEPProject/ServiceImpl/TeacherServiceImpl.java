package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Classroom;
import com.nimblix.SchoolPEPProject.Model.Role;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.ClassroomRepository;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import com.nimblix.SchoolPEPProject.Service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClassroomRepository classroomRepository;

    @Override
    public Map<String, String> registerTeacher(TeacherRegistrationRequest request) {

        Map<String, String> response = new HashMap<>();

        if (request.getFirstName() == null || request.getFirstName().isBlank() ||
                request.getEmail() == null || request.getEmail().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {

            response.put(SchoolConstants.MESSAGE, "Missing required fields (firstName, email, password)");
            return response;
        }

        if (userRepository.existsByEmailId(request.getEmail())) {
            response.put(SchoolConstants.MESSAGE, "Email already registered");
            return response;
        }

        if (teacherRepository.existsByEmailId(request.getEmail())) {
            response.put(SchoolConstants.MESSAGE, "Teacher already exists with this email");
            return response;
        }



        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmailId(request.getEmail());
        user.setPassword(request.getPassword());
        user.setDesignation(SchoolConstants.TEACHER_ROLE);
        user.setStatus(SchoolConstants.ACTIVE);
        user.setMobile(null);
        user.setIsLogin(false);

        Role teacherRole = roleRepository.findByRoleName(SchoolConstants.TEACHER_ROLE);
        user.setRole(teacherRole);

        userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setPrefix(request.getPrefix());
        teacher.setPassword(request.getPassword());
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmailId(request.getEmail());
        teacher.setSchoolId(1L); // TODO: pass schoolId from admin login

        teacherRepository.save(teacher);

        response.put(SchoolConstants.MESSAGE, "Teacher Registered Successfully!");
        return response;
    }

    @Override
    public ResponseEntity<?> getTeacherDetails(Long teacherId) {
     Optional<Teacher> teacher= teacherRepository.findById(teacherId);
     return ResponseEntity.ok(teacher);
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public ResponseEntity<Map<String, String>> createClassroom(ClassroomRequest request) {

        Map<String, String> response = new HashMap<>();

        if (isEmpty(request.getClassroomName())) {
            response.put("status", "FAIL");
            response.put("message", "Classroom name is required");
            return ResponseEntity.badRequest().body(response); // 400
        }
        if (isEmpty(request.getSchoolId())) {
            response.put("status", "FAIL");
            response.put("message", "School ID is required");
            return ResponseEntity.badRequest().body(response); // 400
        }
        if (isEmpty(request.getTeacherId())) {
            response.put("status", "FAIL");
            response.put("message", "Teacher ID is required");
            return ResponseEntity.badRequest().body(response); // 400
        }
        if (isEmpty(request.getSubject())) {
            response.put("status", "FAIL");
            response.put("message", "Subject is required");
            return ResponseEntity.badRequest().body(response); // 400
        }

        List<Classroom> existing = classroomRepository
                .findByClassroomNameAndSchoolId(request.getClassroomName(), request.getSchoolId());

        if (!existing.isEmpty()) {
            response.put("status", "FAIL");
            response.put("message", "Classroom already exists for this school");
            return ResponseEntity.status(409).body(response); // 🔥409 Conflict
        }

        Classroom classroom = new Classroom();
        classroom.setClassroomName(request.getClassroomName());
        classroom.setSchoolId(request.getSchoolId());
        classroom.setTeacherId(request.getTeacherId());
        classroom.setSubject(request.getSubject());
        classroomRepository.save(classroom);

        response.put("status", "SUCCESS");
        response.put("message", "Classroom created successfully");
        return ResponseEntity.ok(response); // 200
    }



}
