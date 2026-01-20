package com.nimblix.SchoolPEPProject.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnalyticsFeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // ================= TASK-4 : FEE TREND (DO NOT CHANGE) =================
    public List<Object[]> fetchFeeTrend(
            Long schoolId,
            String month,
            Long classId,
            String section
    ) {

        String sql = """
            SELECT
                DATE(payment_date) AS pay_date,
                SUM(total_amount) AS total_amt,
                SUM(paid_amount) AS paid_amt,
                SUM(total_amount - paid_amount) AS pending_amt
            FROM fees
            WHERE school_id = :schoolId
              AND DATE_FORMAT(payment_date, '%Y-%m') = :month
        """;

        if (classId != null) {
            sql += " AND class_id = :classId ";
        }

        if (section != null) {
            sql += " AND section = :section ";
        }

        sql += """
            GROUP BY DATE(payment_date)
            ORDER BY pay_date ASC
        """;

        var query = entityManager.createNativeQuery(sql);
        query.setParameter("schoolId", schoolId);
        query.setParameter("month", month);

        if (classId != null) {
            query.setParameter("classId", classId);
        }
        if (section != null) {
            query.setParameter("section", section);
        }

        return query.getResultList();
    }

    // ================= TASK-6 : FEE SUMMARY (NEW ADDITION) =================
    public Object[] fetchFeeSummary(
            Long schoolId,
            String month,
            Long classId,
            String section
    ) {

        String sql = """
            SELECT
                IFNULL(SUM(total_amount), 0) AS total_fee,
                IFNULL(SUM(paid_amount), 0) AS paid_fee,
                IFNULL(SUM(total_amount - paid_amount), 0) AS pending_fee
            FROM fees
            WHERE school_id = :schoolId
              AND DATE_FORMAT(payment_date, '%Y-%m') = :month
        """;

        if (classId != null) {
            sql += " AND class_id = :classId ";
        }

        if (section != null) {
            sql += " AND section = :section ";
        }

        var query = entityManager.createNativeQuery(sql);
        query.setParameter("schoolId", schoolId);
        query.setParameter("month", month);

        if (classId != null) {
            query.setParameter("classId", classId);
        }
        if (section != null) {
            query.setParameter("section", section);
        }

        return (Object[]) query.getSingleResult();
    }
}
