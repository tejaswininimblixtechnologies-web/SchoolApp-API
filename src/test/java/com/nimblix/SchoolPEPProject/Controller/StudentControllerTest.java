//package com.nimblix.SchoolPEPProject.Controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nimblix.SchoolPEPProject.Request.StudentRegistrationRequest;
//import com.nimblix.SchoolPEPProject.Service.StudentService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.doThrow;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(StudentController.class)
//@AutoConfigureMockMvc(addFilters = false)
//public class StudentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private StudentService studentService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    void testStudentRegistrationSuccess() throws Exception {
//
//        StudentRegistrationRequest request = new StudentRegistrationRequest();
//        request.setFullName("John");
//        request.setEmail("john@test.com");
//
//        mockMvc.perform(post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value("Student Registration Successful"));
//    }
//
//    @Test
//    void testStudentRegistrationFailure() throws Exception {
//
//        StudentRegistrationRequest request = new StudentRegistrationRequest();
//        request.setFullName("John");
//
//        doThrow(new RuntimeException("Email already exists"))
//                .when(studentService).registerStudent(Mockito.any());
//
//        mockMvc.perform(post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value("FAILURE"))
//                .andExpect(jsonPath("$.message").value("Email already exists"));
//    }
//}
//
