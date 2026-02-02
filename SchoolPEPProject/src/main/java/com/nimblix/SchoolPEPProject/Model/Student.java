package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", unique = true)
    private String studentId;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "section")
    private String section;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "roll_no")
    private Long rollNo;

    @Column(name = "roll_number")
    private String rollNumber;

    @Column(name = "admission_no")
    private Long admissionNo;

    @Column(name = "registration_no")
    private Long registrationNo;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "parent_contact")
    private String parentContact;

    @Column(name = "parent_email")
    private String parentEmail;

    @ManyToMany(mappedBy = "students")
    private List<Parent> parents = new ArrayList<>();

    // Helper method to get profile photo from parent User class
    public String getProfilePhoto() {
        return super.getProfilePicture();
    }

    // Helper method to set profile photo in parent User class
    public void setProfilePhoto(String profilePhoto) {
        super.setProfilePicture(profilePhoto);
    }
}
