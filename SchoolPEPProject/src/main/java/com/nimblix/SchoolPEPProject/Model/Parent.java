package com.nimblix.SchoolPEPProject.Model;


import com.nimblix.SchoolPEPProject.Enum.ParentRole;
import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parents")
@DiscriminatorValue("PARENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parent extends User {

    @Enumerated(EnumType.STRING)
    private ParentRole parentRole;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "address")
    private String address;
}

