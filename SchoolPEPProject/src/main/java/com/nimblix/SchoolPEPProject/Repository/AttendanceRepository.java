package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    boolean existsBySchoolIdAndStudentIdAndAttendanceDate(
            Long schoolId,
            Long studentId,
            String attendanceDate
    );
    long countBySchoolIdAndAttendanceDate(Long schoolId, String attendanceDate);

    long countBySchoolIdAndAttendanceDateAndAttendanceStatus(
            Long schoolId,
            String attendanceDate,
            String attendanceStatus
    );
    @Query("SELECT a.attendanceDate, " +
            "SUM(CASE WHEN a.attendanceStatus='PRESENT' THEN 1 ELSE 0 END), " +
            "COUNT(a) " +
            "FROM Attendance a " +
            "WHERE a.schoolId = :schoolId AND a.attendanceDate BETWEEN :fromDate AND :toDate " +
            "GROUP BY a.attendanceDate")
    List<Object[]> getAttendanceTrend(@Param("schoolId") Long schoolId,
                                      @Param("fromDate") String fromDate,
                                      @Param("toDate") String toDate);
    @Query("""
        SELECT a.classId,
               a.section,
               COUNT(a.id),
               SUM(CASE WHEN a.attendanceStatus = 'PRESENT' THEN 1 ELSE 0 END),
               SUM(CASE WHEN a.attendanceStatus = 'ABSENT' THEN 1 ELSE 0 END)
        FROM Attendance a
        WHERE a.schoolId = :schoolId
          AND a.attendanceDate = :date
        GROUP BY a.classId, a.section
    """)
    Page<Object[]> getClassWiseAttendance(
            @Param("schoolId") Long schoolId,
            @Param("date") String date,
            Pageable pageable
    );
}
