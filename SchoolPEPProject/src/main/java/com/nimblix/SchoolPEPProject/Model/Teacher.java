package com.nimblix.SchoolPEPProject.Model;



import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

    @Entity
    @Table(name = "teachers")
    @Getter
    @Setter
    @NoArgsConstructor
    public class Teacher {



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "first_name")
        private String firstName;

        @Column(name = "lastName")
        private String lastName;

        @Column(name = "prefix")
        private String prefix;

        @Column(name = "school_id")
        private Long schoolId;

        @Column(name = "email_id")
        private String emailId;

        @Column(name = "password")
        private String password;

        @OneToMany(mappedBy = "teacher")
        private List<Subjects> subjects = new ArrayList<>();

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
