package com.nimblix.SchoolPEPProject.Repository;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnalyticsAcademicQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // ================= TASK 3 =================
    public List<Object[]> fetchAcademicPerformanceTrend(String month) {

        String sql = """
            SELECT
                due_date,
                AVG(marks) AS avg_score,
                (SUM(CASE WHEN marks >= 35 THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) AS pass_percentage
            FROM assignments
            WHERE DATE_FORMAT(due_date, '%Y-%m') = :month
              AND status = 'COMPLETED'
            GROUP BY due_date
            ORDER BY due_date ASC
        """;

        return entityManager
                .createNativeQuery(sql)
                .setParameter("month", month)
                .getResultList();
    }
    
    public Object[] fetchAcademicPerformanceSummary(String month) {

        String sql = """
            SELECT
                COUNT(*) AS total_exams,
                AVG(marks) AS avg_marks,
                MAX(marks) AS max_marks,
                MIN(marks) AS min_marks
            FROM assignments
            WHERE DATE_FORMAT(due_date, '%Y-%m') = :month
              AND status = 'COMPLETED'
        """;

        return (Object[]) entityManager
                .createNativeQuery(sql)
                .setParameter("month", month)
                .getSingleResult();
    }

}
