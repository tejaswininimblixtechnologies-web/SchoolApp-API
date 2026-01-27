package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTeacherId(Long teacherId);

    @Query("SELECT e FROM Event e WHERE e.teacher.id = :teacherId AND e.eventDate = :eventDate")
    List<Event> findByTeacherIdAndEventDate(@Param("teacherId") Long teacherId, 
                                            @Param("eventDate") LocalDate eventDate);

    @Query("SELECT e FROM Event e WHERE e.teacher.id = :teacherId AND e.eventDate BETWEEN :startDate AND :endDate ORDER BY e.eventDate DESC")
    List<Event> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId, 
                                           @Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);

    @Query("SELECT e FROM Event e WHERE e.teacher.id = :teacherId AND e.status = 'ACTIVE' ORDER BY e.eventDate DESC")
    List<Event> findActiveEventsByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT e FROM Event e WHERE e.isSchoolWide = true AND e.status = 'ACTIVE' ORDER BY e.eventDate DESC")
    List<Event> findSchoolWideEvents();

    @Query("SELECT e FROM Event e WHERE (e.teacher.id = :teacherId OR e.isSchoolWide = true) AND e.eventDate >= :currentDate ORDER BY e.eventDate ASC")
    List<Event> findUpcomingEvents(@Param("teacherId") Long teacherId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT e FROM Event e WHERE (e.teacher.id = :teacherId OR e.isSchoolWide = true) AND e.eventDate = :eventDate AND e.status = 'ACTIVE'")
    List<Event> findEventsByDate(@Param("teacherId") Long teacherId, @Param("eventDate") LocalDate eventDate);
}
