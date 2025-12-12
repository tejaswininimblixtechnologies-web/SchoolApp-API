package com.nimblix.SchoolPEPProject.Model;



import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Task")
@Getter
@Setter
@NoArgsConstructor

public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "dueDate")
    private String dueDate;

    @Column(name = "priority")
    private String priority;

    @Column(name = "status")
    private String status;

    @Column(name = "assigned_to")
    private Long UserId;

    @Column(name = "created_time", updatable = false)
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();
        updatedTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();

    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedTime = SchoolUtil.changeCurrentTimeToLocalDateFromGmtToISTInString();


    }

    @OneToMany(mappedBy = "task",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments=new ArrayList<>();


    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachments> attachments = new ArrayList<>();
}




