package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    @Entity
    @Table(name = "Comment")
    @Getter
    @Setter
    @NoArgsConstructor
    public class Comment {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


        @Column(name = "message")
        private String message;

        @ManyToOne
        @JoinColumn(name = "task_id")
        private Task task;

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
