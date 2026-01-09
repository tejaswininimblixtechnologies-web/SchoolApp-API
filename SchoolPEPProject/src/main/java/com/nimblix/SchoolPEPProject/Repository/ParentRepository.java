package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent,Long> {

    boolean existsByEmailId(String emailId);
}
