package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Model.StudentMarks;
import com.nimblix.SchoolPEPProject.Request.StudentMarksRequest;
import com.nimblix.SchoolPEPProject.Service.StudentMarksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/marks")
public class StudentMarksController {

    private final StudentMarksService studentMarksService;

    // Get marks
    @GetMapping("/{studentId}")
    public ResponseEntity<List<StudentMarks>> getMarks(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentMarksService.getMarksByStudentId(studentId));
    }

    // Download CSV
    @GetMapping("/{studentId}/download")
    public ResponseEntity<byte[]> downloadMarks(@PathVariable Long studentId) {

        List<StudentMarks> marks = studentMarksService.getMarksByStudentId(studentId);

        String csv = "Subject,Marks,Grade,ExamType\n" +
                marks.stream()
                        .map(m -> m.getSubject() + "," + m.getMarks() + "," + m.getGrade() + "," + m.getExamType())
                        .collect(Collectors.joining("\n"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=marks.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.getBytes(StandardCharsets.UTF_8));
    }

    @PostMapping
    public ResponseEntity<StudentMarks> addMarks(@RequestBody StudentMarksRequest request) {
        return ResponseEntity.ok(studentMarksService.saveOrUpdateMarks(request));
    }
}
