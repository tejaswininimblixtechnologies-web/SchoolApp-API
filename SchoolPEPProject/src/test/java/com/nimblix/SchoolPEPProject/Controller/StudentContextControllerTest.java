package com.nimblix.SchoolPEPProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
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
public class StudentContextControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "student@example.com", roles = {"STUDENT"})
    void testGetStudentContextSuccess() throws Exception {

        StudentContextResponse mockResponse = StudentContextResponse.builder()
                .studentId(123L)
                .classId(10L)
                .section("A")
                .academicYear("2024-2025")
                .build();

        when(studentService.getStudentContext("student@example.com")).thenReturn(mockResponse);

        mockMvc.perform(get("/student/context")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(SchoolConstants.STATUS_SUCCESS))
                .andExpect(jsonPath("$.message").value("Student context fetched successfully"))
                .andExpect(jsonPath("$.data.studentId").value(123))
                .andExpect(jsonPath("$.data.classId").value(10))
                .andExpect(jsonPath("$.data.section").value("A"))
                .andExpect(jsonPath("$.data.academicYear").value("2024-2025"));
    }

    @Test
    @WithMockUser(username = "student@example.com", roles = {"STUDENT"})
    void testGetStudentContextFailure() throws Exception {

        when(studentService.getStudentContext("student@example.com"))
                .thenThrow(new RuntimeException("Student not found"));

        mockMvc.perform(get("/student/context")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(SchoolConstants.STATUS_FAILURE))
                .andExpect(jsonPath("$.message").value("Student not found"));
    }
}
