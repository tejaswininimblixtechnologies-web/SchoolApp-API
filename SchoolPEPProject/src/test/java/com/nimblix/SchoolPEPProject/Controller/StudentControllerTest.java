package com.nimblix.SchoolPEPProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblix.SchoolPEPProject.Model.Subjects;
import com.nimblix.SchoolPEPProject.Response.SubjectResponse;
import com.nimblix.SchoolPEPProject.Response.StudentContextResponse;
import com.nimblix.SchoolPEPProject.Service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class, 
            excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
            })
@ContextConfiguration(classes = {StudentController.class})
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "student@test.com", roles = {"STUDENT"})
    void testGetStudentDetails() throws Exception {

        StudentContextResponse mockResponse = StudentContextResponse.builder()
                .studentId(1L)
                .firstName("Test")
                .lastName("Student")
                .email("student@test.com")
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .rollNumber("STU001")
                .schoolId(1L)
                .build();

        when(studentService.getStudentContext("student@test.com")).thenReturn(mockResponse);

        mockMvc.perform(get("/student/logged-in-details")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Student details fetched successfully"))
                .andExpect(jsonPath("$.data.studentId").value(1))
                .andExpect(jsonPath("$.data.firstName").value("Test"))
                .andExpect(jsonPath("$.data.lastName").value("Student"))
                .andExpect(jsonPath("$.data.email").value("student@test.com"))
                .andExpect(jsonPath("$.data.classId").value(10))
                .andExpect(jsonPath("$.data.section").value("A"));
    }

    @Test
    @WithMockUser(username = "student@test.com", roles = {"STUDENT"})
    void testGetStudentSubjects() throws Exception {

        List<SubjectResponse> mockSubjects = Arrays.asList(
                SubjectResponse.builder()
                        .id(1L)
                        .subjectName("Mathematics")
                        .code("MATH101")
                        .subDescription("Mathematics subject")
                        .classRoomId(10L)
                        .totalMarks(100L)
                        .marksObtained(85L)
                        .build(),
                SubjectResponse.builder()
                        .id(2L)
                        .subjectName("English")
                        .code("ENG101")
                        .subDescription("English subject")
                        .classRoomId(10L)
                        .totalMarks(100L)
                        .marksObtained(90L)
                        .build()
        );

        when(studentService.getStudentSubjects("student@test.com")).thenReturn(mockSubjects);

        mockMvc.perform(get("/student/subjects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Student subjects fetched successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].subjectName").value("Mathematics"))
                .andExpect(jsonPath("$.data[0].code").value("MATH101"))
                .andExpect(jsonPath("$.data[1].subjectName").value("English"))
                .andExpect(jsonPath("$.data[1].code").value("ENG101"));
    }

    @Test
    @WithMockUser(username = "student@test.com", roles = {"STUDENT"})
    void testGetStudentDetailsFailure() throws Exception {

        when(studentService.getStudentContext("student@test.com"))
                .thenThrow(new RuntimeException("Student not found"));

        mockMvc.perform(get("/student/logged-in-details")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILURE"))
                .andExpect(jsonPath("$.message").value("Student not found"));
    }
}
