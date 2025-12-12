package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdminService {
    String submitEmail(String email);
    Long createAdminAccount(AdminAccountCreateRequest request);
    List<Student> getStudentList(Long schoolId, Long classId, String section, String status);
}
