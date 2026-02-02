package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    @Query("SELECT t FROM Timetable t WHERE t.classId = :classId AND t.section = :section AND t.academicYear = :academicYear ORDER BY t.dayOfWeek, t.periodNumber")
    List<Timetable> findByClassSectionAndAcademicYear(@Param("classId") Long classId, 
                                                     @Param("section") String section, 
                                                     @Param("academicYear") String academicYear);

    @Query("SELECT t FROM Timetable t WHERE t.classId = :classId AND t.section = :section AND t.academicYear = :academicYear AND t.dayOfWeek = :dayOfWeek ORDER BY t.periodNumber")
    List<Timetable> findByClassSectionAcademicYearAndDay(@Param("classId") Long classId, 
                                                         @Param("section") String section, 
                                                         @Param("academicYear") String academicYear,
                                                         @Param("dayOfWeek") String dayOfWeek);
}
