package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.ClassesConductedRequest;
import com.nimblix.SchoolPEPProject.Response.ClassesConductedResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface ClassesConductedService {

    // CRUD Operations
    ResponseEntity<ClassesConductedResponse> createClass(ClassesConductedRequest request);
    ResponseEntity<ClassesConductedResponse> updateClass(Long classId, ClassesConductedRequest request);
    ResponseEntity<String> deleteClass(Long classId);
    ResponseEntity<ClassesConductedResponse> getClassById(Long classId);

    // Teacher Classes Operations
    ResponseEntity<List<ClassesConductedResponse>> getTeacherClasses();
    ResponseEntity<List<ClassesConductedResponse>> getClassesByDate(LocalDate classDate);
    ResponseEntity<List<ClassesConductedResponse>> getClassesByDateRange(LocalDate startDate, LocalDate endDate);
    ResponseEntity<List<ClassesConductedResponse>> getTodayClasses();
}
