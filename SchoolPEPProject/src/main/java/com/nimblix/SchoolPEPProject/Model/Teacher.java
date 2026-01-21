package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Enum.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "teachers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Basic Details */

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(name = "email_id", nullable = false, unique = true)
    private String emailId;

    @Column(nullable = false)
    private String mobile;

    /* Academic Details */

    private Long subjectId;

    private String subjectName;

    private String designation;

    /* School Mapping */

    @Column(nullable = false)
    private Long schoolId;

    private String gender;

    /* System Fields */

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    /* Auto-handled fields */

    @PrePersist
    protected void onCreate() {
        this.status = Status.ACTIVE;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
}
