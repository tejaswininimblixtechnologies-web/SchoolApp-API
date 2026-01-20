package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnalyticsAttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("""
        SELECT 
            a.attendanceDate,
            COUNT(a.studentId),
            SUM(CASE WHEN a.attendanceStatus = 'PRESENT' THEN 1 ELSE 0 END),
            SUM(CASE WHEN a.attendanceStatus = 'ABSENT' THEN 1 ELSE 0 END)
        FROM Attendance a
        WHERE a.attendanceDate LIKE CONCAT(:month, '%')
        GROUP BY a.attendanceDate
        ORDER BY a.attendanceDate ASC
    """)
    List<Object[]> fetchAttendanceTrend(@Param("month") String month);
    
    
    @Query("""
    	    SELECT
    	        SUM(CASE WHEN a.attendanceStatus = 'PRESENT' THEN 1 ELSE 0 END),
    	        SUM(CASE WHEN a.attendanceStatus = 'ABSENT' THEN 1 ELSE 0 END),
    	        COUNT(DISTINCT a.attendanceDate)
    	    FROM Attendance a
    	    WHERE a.attendanceDate LIKE CONCAT(:month, '%')
    	""")
    	List<Object[]> fetchAttendanceSummary(@Param("month") String month);


}
