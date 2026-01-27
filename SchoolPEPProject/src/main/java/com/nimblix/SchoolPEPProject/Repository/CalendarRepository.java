package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Calendar;
import com.nimblix.SchoolPEPProject.Enum.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    List<Calendar> findByTeacherId(Long teacherId);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.status = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findActiveEventsByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.startDateTime BETWEEN :startDate AND :endDate ORDER BY c.startDateTime ASC")
    List<Calendar> findEventsByTeacherIdAndDateRange(@Param("teacherId") Long teacherId, 
                                                     @Param("startDate") LocalDateTime startDate, 
                                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.eventType = :eventType AND c.status = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findEventsByTeacherIdAndEventType(@Param("teacherId") Long teacherId, 
                                                     @Param("eventType") EventType eventType);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.classroom.id = :classroomId AND c.status = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findEventsByTeacherIdAndClassroomId(@Param("teacherId") Long teacherId, 
                                                        @Param("classroomId") Long classroomId);

    @Query("SELECT c FROM Calendar c WHERE c.startDateTime >= :currentDateTime AND c.status = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findUpcomingEvents(@Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.startDateTime >= :currentDateTime AND c.status = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findUpcomingEventsByTeacherId(@Param("teacherId") Long teacherId, 
                                                  @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.startDateTime BETWEEN :startDate AND :endDate AND c.status = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findEventsByTeacherIdInMonth(@Param("teacherId") Long teacherId, 
                                                @Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);

    boolean existsByTeacherIdAndEventTitleAndStartDateTime(Long teacherId, String eventTitle, LocalDateTime startDateTime);
}
