package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByTeacherId(Long teacherId);

    @Query("SELECT m FROM Meeting m WHERE m.teacher.id = :teacherId AND m.meetingDate = :meetingDate")
    List<Meeting> findByTeacherIdAndMeetingDate(@Param("teacherId") Long teacherId, 
                                               @Param("meetingDate") LocalDate meetingDate);

    @Query("SELECT m FROM Meeting m WHERE m.teacher.id = :teacherId AND m.meetingDate BETWEEN :startDate AND :endDate ORDER BY m.meetingDate DESC")
    List<Meeting> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId, 
                                              @Param("startDate") LocalDate startDate, 
                                              @Param("endDate") LocalDate endDate);

    @Query("SELECT m FROM Meeting m WHERE m.teacher.id = :teacherId AND m.status = 'ACTIVE' ORDER BY m.meetingDate DESC")
    List<Meeting> findActiveMeetingsByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT m FROM Meeting m WHERE m.teacher.id = :teacherId AND m.meetingDate = :meetingDate AND m.status = 'ACTIVE'")
    List<Meeting> findActiveMeetingsByTeacherIdAndDate(@Param("teacherId") Long teacherId, 
                                                      @Param("meetingDate") LocalDate meetingDate);
}
