package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.StudentMarks;
import com.nimblix.SchoolPEPProject.Repository.StudentMarksRepository;
import com.nimblix.SchoolPEPProject.Request.StudentMarksRequest;
import com.nimblix.SchoolPEPProject.Service.StudentMarksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentMarksServiceImpl implements StudentMarksService {

    private final StudentMarksRepository studentMarksRepository;

    @Override
    public List<StudentMarks> getMarksByStudentId(Long studentId) {
        return studentMarksRepository.findByStudentId(studentId);
    }

    @Override
    public StudentMarks saveOrUpdateMarks(StudentMarksRequest request) {

        StudentMarks marks = StudentMarks.builder()
                .studentId(request.getStudentId())
                .subject(request.getSubject())
                .marks(request.getMarks())
                .grade(request.getGrade())
                .examType(request.getExamType())
                .build();

        return studentMarksRepository.save(marks);
    }
}