package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository interface for Admin entity.
 * Provides database access methods for admin-related operations.
 */

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {

    boolean existsByEmailId(String email);

    Admin findByIdAndSchoolId(Long adminId, Long schoolId);

    boolean existsByMobile(String adminMobileNo);

    Optional<Admin> findByEmailId(String email);
}
