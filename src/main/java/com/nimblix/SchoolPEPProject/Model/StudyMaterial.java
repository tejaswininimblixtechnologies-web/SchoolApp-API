package com.nimblix.SchoolPEPProject.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "study_materials")
@Getter
@Setter
public class StudyMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String fileUrl;
    private LocalDateTime createdTime = LocalDateTime.now();

    // Relation with Teacher
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonIgnoreProperties({"password", "emailId", "subjects", "classrooms"})
    private Teacher teacher;

    // Relation with Subject
    @ManyToOne
    @JoinColumn(name = "subject_id")
    @JsonIgnoreProperties({"teacher", "studyMaterials"})
    private Subjects subject;

    // Relation with Classroom
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    @JsonIgnoreProperties({"students", "subjects"})
    private Classroom classroom;
}