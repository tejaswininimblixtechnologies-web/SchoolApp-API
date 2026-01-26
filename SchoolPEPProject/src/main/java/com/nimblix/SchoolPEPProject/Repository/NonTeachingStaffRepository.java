package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.NonTeachingStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NonTeachingStaffRepository extends JpaRepository<NonTeachingStaff, Long> {
    Optional<NonTeachingStaff> findByEmailId(String emailId);
}
