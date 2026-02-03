package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Calendar;
import com.nimblix.SchoolPEPProject.Enum.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId")
    List<Calendar> findByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.eventStatus = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findActiveEventsByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.startDateTime BETWEEN :startDate AND :endDate ORDER BY c.startDateTime ASC")
    List<Calendar> findEventsByTeacherIdAndDateRange(@Param("teacherId") Long teacherId, 
                                                     @Param("startDate") LocalDateTime startDate, 
                                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.eventType = :eventType AND c.eventStatus = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findEventsByTeacherIdAndEventType(@Param("teacherId") Long teacherId, 
                                                     @Param("eventType") EventType eventType);

    // ========== MEETING-SPECIFIC QUERIES ==========
    
    // Find meetings by teacher ID
    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.eventType = 'MEETING' AND c.meetingStatus != 'DELETED' ORDER BY c.startDateTime ASC")
    List<Calendar> findByTeacherIdAndEventType(@Param("teacherId") Long teacherId, EventType eventType);

    // Find meetings by teacher ID and date
    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.eventType = :eventType AND c.eventDate = :eventDate AND c.meetingStatus != 'DELETED' ORDER BY c.startDateTime ASC")
    List<Calendar> findByTeacherIdAndEventTypeAndEventDate(@Param("teacherId") Long teacherId, 
                                                             @Param("eventType") EventType eventType, 
                                                             @Param("eventDate") LocalDate eventDate);

    // Find meetings by teacher ID and date range
    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.eventType = :eventType AND c.eventDate BETWEEN :startDate AND :endDate AND c.meetingStatus != 'DELETED' ORDER BY c.startDateTime ASC")
    List<Calendar> findByTeacherIdAndEventTypeAndEventDateBetween(@Param("teacherId") Long teacherId, 
                                                                     @Param("eventType") EventType eventType, 
                                                                     @Param("startDate") LocalDate startDate, 
                                                                     @Param("endDate") LocalDate endDate);

    // Find all meetings (not deleted)
    @Query("SELECT c FROM Calendar c WHERE c.eventType = 'MEETING' AND c.meetingStatus != 'DELETED' ORDER BY c.startDateTime ASC")
    List<Calendar> findAllMeetings();

    // Find meetings by date
    @Query("SELECT c FROM Calendar c WHERE c.eventType = 'MEETING' AND c.eventDate = :eventDate AND c.meetingStatus != 'DELETED' ORDER BY c.startDateTime ASC")
    List<Calendar> findMeetingsByEventDate(@Param("eventDate") LocalDate eventDate);

    // Find upcoming meetings
    @Query("SELECT c FROM Calendar c WHERE c.eventType = 'MEETING' AND c.eventDate > :currentDate AND c.meetingStatus != 'DELETED' ORDER BY c.startDateTime ASC")
    List<Calendar> findUpcomingMeetings(@Param("currentDate") LocalDate currentDate);

    // Find today's meetings
    @Query("SELECT c FROM Calendar c WHERE c.eventType = 'MEETING' AND c.eventDate = :currentDate AND c.meetingStatus != 'DELETED' ORDER BY c.startDateTime ASC")
    List<Calendar> findTodayMeetings(@Param("currentDate") LocalDate currentDate);

    // Find meetings by status
    @Query("SELECT c FROM Calendar c WHERE c.eventType = 'MEETING' AND c.meetingStatus = :status ORDER BY c.startDateTime ASC")
    List<Calendar> findMeetingsByStatus(@Param("status") String status);

    // Find school-wide events
    @Query("SELECT c FROM Calendar c WHERE c.schoolWide = true AND c.eventStatus = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findSchoolWideEvents();

    // Find events by applicable classes
    @Query("SELECT c FROM Calendar c WHERE c.applicableClasses LIKE %:classId% AND c.eventStatus = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findEventsByApplicableClass(@Param("classId") Long classId);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.classroom.id = :classroomId AND c.eventStatus = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findEventsByTeacherIdAndClassroomId(@Param("teacherId") Long teacherId, 
                                                        @Param("classroomId") Long classroomId);

    @Query("SELECT c FROM Calendar c WHERE c.startDateTime >= :currentDateTime AND c.eventStatus = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findUpcomingEvents(@Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.startDateTime >= :currentDateTime AND c.eventStatus = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findUpcomingEventsByTeacherId(@Param("teacherId") Long teacherId, 
                                                  @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.startDateTime BETWEEN :startDate AND :endDate AND c.eventStatus = 'ACTIVE' ORDER BY c.startDateTime ASC")
    List<Calendar> findEventsByTeacherIdInMonth(@Param("teacherId") Long teacherId, 
                                                @Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Calendar c WHERE c.teacher.id = :teacherId AND c.eventTitle = :eventTitle AND c.startDateTime = :startDateTime")
    boolean existsByTeacherIdAndEventTitleAndStartDateTime(@Param("teacherId") Long teacherId, @Param("eventTitle") String eventTitle, @Param("startDateTime") LocalDateTime startDateTime);
}
