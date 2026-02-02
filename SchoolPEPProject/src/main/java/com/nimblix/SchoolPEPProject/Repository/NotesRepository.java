package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {

    // Find notes by teacher ID
    List<Notes> findByTeacherId(Long teacherId);

    // Find notes by teacher ID and note type
    List<Notes> findByTeacherIdAndNoteType(Long teacherId, String noteType);

    // Find notes by teacher ID and note status
    List<Notes> findByTeacherIdAndNoteStatus(Long teacherId, String noteStatus);

    // Find notes by teacher ID, note type, and status
    List<Notes> findByTeacherIdAndNoteTypeAndNoteStatus(Long teacherId, String noteType, String noteStatus);

    // Find notes by note date
    List<Notes> findByNoteDate(LocalDate noteDate);

    // Find notes by teacher ID and note date
    List<Notes> findByTeacherIdAndNoteDate(Long teacherId, LocalDate noteDate);

    // Find notes by date range
    @Query("SELECT n FROM Notes n WHERE n.noteDate BETWEEN :startDate AND :endDate")
    List<Notes> findByNoteDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find notes by teacher ID and date range
    @Query("SELECT n FROM Notes n WHERE n.teacherId = :teacherId AND n.noteDate BETWEEN :startDate AND :endDate")
    List<Notes> findByTeacherIdAndNoteDateBetween(@Param("teacherId") Long teacherId, 
                                                   @Param("startDate") LocalDate startDate, 
                                                   @Param("endDate") LocalDate endDate);

    // Find active notes (not deleted)
    @Query("SELECT n FROM Notes n WHERE n.noteStatus != 'DELETED'")
    List<Notes> findActiveNotes();

    // Find active notes by teacher
    @Query("SELECT n FROM Notes n WHERE n.teacherId = :teacherId AND n.noteStatus != 'DELETED'")
    List<Notes> findActiveNotesByTeacher(@Param("teacherId") Long teacherId);

    // Find diary entries specifically
    @Query("SELECT n FROM Notes n WHERE n.noteType = 'DIARY' AND n.noteStatus != 'DELETED'")
    List<Notes> findDiaryEntries();

    // Find diary entries by teacher
    @Query("SELECT n FROM Notes n WHERE n.teacherId = :teacherId AND n.noteType = 'DIARY' AND n.noteStatus != 'DELETED'")
    List<Notes> findDiaryEntriesByTeacher(@Param("teacherId") Long teacherId);

    // Find quick notes specifically
    @Query("SELECT n FROM Notes n WHERE n.noteType = 'QUICK' AND n.noteStatus != 'DELETED'")
    List<Notes> findQuickNotes();

    // Find quick notes by teacher
    @Query("SELECT n FROM Notes n WHERE n.teacherId = :teacherId AND n.noteType = 'QUICK' AND n.noteStatus != 'DELETED'")
    List<Notes> findQuickNotesByTeacher(@Param("teacherId") Long teacherId);

    // Find notes by priority
    List<Notes> findByPriority(String priority);

    // Find upcoming notes (notes with future dates)
    @Query("SELECT n FROM Notes n WHERE n.noteDate > :currentDate AND n.noteStatus != 'DELETED'")
    List<Notes> findUpcomingNotes(@Param("currentDate") LocalDate currentDate);

    // Find today's notes
    @Query("SELECT n FROM Notes n WHERE n.noteDate = :currentDate AND n.noteStatus != 'DELETED'")
    List<Notes> findTodayNotes(@Param("currentDate") LocalDate currentDate);
}
