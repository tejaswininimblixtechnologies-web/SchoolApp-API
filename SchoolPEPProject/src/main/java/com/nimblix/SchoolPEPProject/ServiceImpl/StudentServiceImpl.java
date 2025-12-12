package com.nimblix.SchoolPEPProject.ServiceImpl;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Role;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.RoleRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import com.nimblix.SchoolPEPProject.Request.StudentRegistrationRequest;
import com.nimblix.SchoolPEPProject.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;

    public ResponseEntity<?> registerStudent(StudentRegistrationRequest request) { // Validate password match
        if (!request.getPassword().equals(request.getReEnterPassword())) {
            return ResponseEntity.badRequest().body("Password and Re-Enter Password do not match!");
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Save Student
        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setPassword(encodedPassword);
        student.setSchoolId(request.getSchoolId());
        student.setStatus(SchoolConstants.ACTIVE);

        studentRepository.save(student);

        Role studentRole = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Save User (Login table)
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmailId(request.getEmail());
        user.setPassword(encodedPassword);
        user.setStatus(SchoolConstants.ACTIVE);
        user.setIsLogin(false);
        user.setRole(studentRole);
        user.setDesignation(SchoolConstants.STUDENT);
        userRepository.save(user);

        return ResponseEntity.ok("Registration Successful");
    }

    @Override
    public Student getStudentListByStudentId(Long studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    @Override
    public void updateStudentDetails(Long studentId, StudentRegistrationRequest request) {

        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        if (request.getFullName() != null) {
            existingStudent.setFullName(request.getFullName());
        }

        if (request.getEmail() != null) {
            existingStudent.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {

            if (!request.getPassword().equals(request.getReEnterPassword())) {
                throw new RuntimeException("Password and Re-Enter Password do not match!");
            }

            String encodedPassword = passwordEncoder.encode(request.getPassword());
            existingStudent.setPassword(encodedPassword);
        }

        if (request.getSchoolId() != null) {
            existingStudent.setSchoolId(request.getSchoolId());
        }

        studentRepository.save(existingStudent);
    }


//    @Override
//    public void deleteStudent(Long studentId) {
//
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
//
//        studentRepository.delete(student);
//    }


    @Override
    public void deleteStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
        student.setStatus(SchoolConstants.IN_ACTIVE);

        studentRepository.save(student);
    }
}
