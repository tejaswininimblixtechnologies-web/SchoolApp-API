package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    List<DiaryEntry> findByTeacherId(Long teacherId);

    List<DiaryEntry> findByTeacherIdAndEntryDate(Long teacherId, LocalDate entryDate);

    @Query("SELECT d FROM DiaryEntry d WHERE d.teacher.id = :teacherId AND d.status = 'ACTIVE' ORDER BY d.entryDate DESC")
    List<DiaryEntry> findActiveEntriesByTeacherId(@Param("teacherId") Long teacherId);
}
