package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.ClassesConducted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassesConductedRepository extends JpaRepository<ClassesConducted, Long> {

    List<ClassesConducted> findByTeacherId(Long teacherId);

    @Query("SELECT c FROM ClassesConducted c WHERE c.teacher.id = :teacherId AND c.classDate = :classDate")
    List<ClassesConducted> findByTeacherIdAndClassDate(@Param("teacherId") Long teacherId, 
                                                       @Param("classDate") LocalDate classDate);

    @Query("SELECT c FROM ClassesConducted c WHERE c.teacher.id = :teacherId AND c.classDate BETWEEN :startDate AND :endDate ORDER BY c.classDate DESC")
    List<ClassesConducted> findByTeacherIdAndDateRange(@Param("teacherId") Long teacherId, 
                                                       @Param("startDate") LocalDate startDate, 
                                                       @Param("endDate") LocalDate endDate);

    @Query("SELECT c FROM ClassesConducted c WHERE c.teacher.id = :teacherId AND c.status = 'ACTIVE' ORDER BY c.classDate DESC")
    List<ClassesConducted> findActiveClassesByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT c FROM ClassesConducted c WHERE c.teacher.id = :teacherId AND c.classDate = :classDate AND c.status = 'ACTIVE'")
    List<ClassesConducted> findActiveClassesByTeacherIdAndDate(@Param("teacherId") Long teacherId, 
                                                              @Param("classDate") LocalDate classDate);
}
