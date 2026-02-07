package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_marks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentMarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "marks")
    private Double marks;

    @Column(name = "grade")
    private String grade;

    @Column(name = "exam_type")
    private String examType;
}