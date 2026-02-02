package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.TimetableNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableNoteRepository extends JpaRepository<TimetableNote, Long> {

    @Query("SELECT tn FROM TimetableNote tn WHERE tn.classId = :classId AND tn.section = :section AND tn.subject = :subject AND tn.dayOfWeek = :dayOfWeek AND tn.periodNumber = :periodNumber")
    List<TimetableNote> findByClassSectionSubjectDayAndPeriod(@Param("classId") Long classId,
                                                             @Param("section") String section,
                                                             @Param("subject") String subject,
                                                             @Param("dayOfWeek") String dayOfWeek,
                                                             @Param("periodNumber") Integer periodNumber);

    @Query("SELECT tn FROM TimetableNote tn WHERE tn.classId = :classId AND tn.section = :section AND tn.dayOfWeek = :dayOfWeek")
    List<TimetableNote> findByClassSectionAndDay(@Param("classId") Long classId,
                                                @Param("section") String section,
                                                @Param("dayOfWeek") String dayOfWeek);

    @Query("SELECT tn FROM TimetableNote tn WHERE tn.classId = :classId AND tn.section = :section")
    List<TimetableNote> findByClassAndSection(@Param("classId") Long classId,
                                             @Param("section") String section);
}
