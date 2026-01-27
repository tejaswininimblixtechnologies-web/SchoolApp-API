package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.QuickNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuickNoteRepository extends JpaRepository<QuickNote, Long> {

    List<QuickNote> findByTeacherId(Long teacherId);

    @Query("SELECT q FROM QuickNote q WHERE q.teacher.id = :teacherId AND q.noteDate = :noteDate")
    List<QuickNote> findByTeacherIdAndNoteDate(@Param("teacherId") Long teacherId, 
                                              @Param("noteDate") LocalDate noteDate);

    @Query("SELECT q FROM QuickNote q WHERE q.teacher.id = :teacherId AND q.noteDate BETWEEN :startDate AND :endDate ORDER BY q.noteDate DESC")
    List<QuickNote> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId, 
                                               @Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate);

    @Query("SELECT q FROM QuickNote q WHERE q.teacher.id = :teacherId AND q.status = 'ACTIVE' ORDER BY q.noteDate DESC, q.priority ASC")
    List<QuickNote> findActiveNotesByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT q FROM QuickNote q WHERE q.teacher.id = :teacherId AND q.noteDate = :noteDate AND q.status = 'ACTIVE' ORDER BY q.priority ASC")
    List<QuickNote> findActiveNotesByTeacherIdAndDate(@Param("teacherId") Long teacherId, 
                                                    @Param("noteDate") LocalDate noteDate);

    @Query("SELECT q FROM QuickNote q WHERE q.teacher.id = :teacherId AND q.noteDate >= :currentDate ORDER BY q.noteDate ASC, q.priority ASC")
    List<QuickNote> findUpcomingNotes(@Param("teacherId") Long teacherId, @Param("currentDate") LocalDate currentDate);
}
