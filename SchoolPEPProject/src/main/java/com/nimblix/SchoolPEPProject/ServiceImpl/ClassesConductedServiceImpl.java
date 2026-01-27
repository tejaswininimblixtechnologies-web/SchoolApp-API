package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.ClassesConducted;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Repository.ClassesConductedRepository;
import com.nimblix.SchoolPEPProject.Repository.TeacherRepository;
import com.nimblix.SchoolPEPProject.Request.ClassesConductedRequest;
import com.nimblix.SchoolPEPProject.Response.ClassesConductedResponse;
import com.nimblix.SchoolPEPProject.Service.ClassesConductedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClassesConductedServiceImpl implements ClassesConductedService {

    private final ClassesConductedRepository classesConductedRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<ClassesConductedResponse> createClass(ClassesConductedRequest request) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Creating class conducted for teacher: {} on date: {}", teacher.getId(), request.getClassDate());

            // For now, create a simple response without database operations
            ClassesConductedResponse response = ClassesConductedResponse.builder()
                    .id(1L)
                    .classDate(request.getClassDate())
                    .classroomId(request.getClassroomId())
                    .classroomName("Test Classroom")
                    .section(request.getSection())
                    .subjectId(request.getSubjectId())
                    .subjectName("Test Subject")
                    .subjectCode("TEST101")
                    .periodDuration(request.getPeriodDuration())
                    .remarks(request.getRemarks())
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build();

            log.info("Class conducted created successfully (test mode)");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error creating class conducted: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Helper method
    private Teacher getAuthenticatedTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        return teacherRepository.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    // Other methods - simplified for now
    @Override
    public ResponseEntity<ClassesConductedResponse> updateClass(Long classId, ClassesConductedRequest request) {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> deleteClass(Long classId) {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<ClassesConductedResponse> getClassById(Long classId) {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<ClassesConductedResponse>> getTeacherClasses() {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<List<ClassesConductedResponse>> getClassesByDate(LocalDate classDate) {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            log.info("Getting classes for teacher: {} on date: {}", teacher.getId(), classDate);

            // For now, return sample data for testing
            List<ClassesConductedResponse> sampleClasses = List.of(
                ClassesConductedResponse.builder()
                    .id(1L)
                    .classDate(classDate)
                    .classroomId(1L)
                    .classroomName("10th Grade - Section A")
                    .section("A")
                    .subjectId(1L)
                    .subjectName("Mathematics")
                    .subjectCode("MATH101")
                    .periodDuration("Period 1 (9:00-10:00 AM)")
                    .remarks("Students were very engaged in learning algebraic expressions")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build(),
                ClassesConductedResponse.builder()
                    .id(2L)
                    .classDate(classDate)
                    .classroomId(1L)
                    .classroomName("10th Grade - Section A")
                    .section("A")
                    .subjectId(2L)
                    .subjectName("Physics")
                    .subjectCode("PHY101")
                    .periodDuration("Period 2 (10:30-11:30 AM)")
                    .remarks("Introduced Newton's laws of motion")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build()
            );

            return ResponseEntity.ok(sampleClasses);

        } catch (Exception e) {
            log.error("Error getting classes by date: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<ClassesConductedResponse>> getTodayClasses() {
        try {
            Teacher teacher = getAuthenticatedTeacher();
            LocalDate today = LocalDate.now();
            log.info("Getting today's classes for teacher: {} on date: {}", teacher.getId(), today);

            // For now, return sample data for today
            List<ClassesConductedResponse> todayClasses = List.of(
                ClassesConductedResponse.builder()
                    .id(3L)
                    .classDate(today)
                    .classroomId(1L)
                    .classroomName("10th Grade - Section A")
                    .section("A")
                    .subjectId(1L)
                    .subjectName("Mathematics")
                    .subjectCode("MATH101")
                    .periodDuration("Period 1 (9:00-10:00 AM)")
                    .remarks("Today's math class - quadratic equations")
                    .status("ACTIVE")
                    .teacherId(teacher.getId())
                    .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                    .build()
            );

            return ResponseEntity.ok(todayClasses);

        } catch (Exception e) {
            log.error("Error getting today's classes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<ClassesConductedResponse>> getClassesByDateRange(LocalDate startDate, LocalDate endDate) {
        return ResponseEntity.ok(List.of());
    }
}
