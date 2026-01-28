package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.FeesPayment;
import com.nimblix.SchoolPEPProject.Enum.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeesPaymentRepository extends JpaRepository<FeesPayment, Long> {

    @Query("""
        SELECT COALESCE(SUM(fp.amount), 0)
        FROM FeesPayment fp
        WHERE fp.school.id = :schoolId
    """)
    Double getTotalAssignedFees(@Param("schoolId") Long schoolId);

    @Query("""
        SELECT COALESCE(SUM(fp.amount), 0)
        FROM FeesPayment fp
        WHERE fp.school.id = :schoolId
        AND fp.status = :status
    """)
    Double getTotalPaidFees(@Param("schoolId") Long schoolId,
                            @Param("status") PaymentStatus status);
}
