package com.nimblix.SchoolPEPProject.Repository;
import java.util.List;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByStudentIdAndAttendanceDate(Long studentId, String attendanceDate);

    long countByAttendanceDateAndAttendanceStatus(String attendanceDate, String attendanceStatus);

    @Query("""
    SELECT a.attendanceDate,
           COUNT(a.id),
           SUM(CASE WHEN a.attendanceStatus = 'PRESENT' THEN 1 ELSE 0 END)
    FROM Attendance a
    WHERE a.attendanceDate BETWEEN :fromDate AND :toDate
    GROUP BY a.attendanceDate
    ORDER BY a.attendanceDate ASC
""")
    List<Object[]> getAttendanceTrend(
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate
    );

}
