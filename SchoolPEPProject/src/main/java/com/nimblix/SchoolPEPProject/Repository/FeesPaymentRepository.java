package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.FeesPayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeesPaymentRepository extends JpaRepository<FeesPayment, Long> {

    @Query("""
        SELECT COALESCE(SUM(f.amount), 0)
        FROM FeesPayment f
        WHERE f.school.id = :schoolId
        AND f.status = 'PAID'
    """)
    Double getTotalCollectedFees(@Param("schoolId") Long schoolId);
}
