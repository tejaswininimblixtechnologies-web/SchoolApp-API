package com.nimblix.SchoolPEPProject.Model;


import com.nimblix.SchoolPEPProject.Enum.ParentRole;
import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parent {





    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parentId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name="email_id" ,unique = true)
    private String emailId;


    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name="address")
    private String address;

    @Column(name = "password")
    private String password;

    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "parentRole")
    @Enumerated(EnumType.STRING)
    private ParentRole role;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;


    @PrePersist
    protected void onCreate(){
        createdTime= SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
        updatedTime= SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();

    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedTime= SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();


    }


}