package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByEmailId(String emailId);

    Optional<Teacher> findByEmailId(String emailId);

    Teacher findByIdAndSchoolId(Long id, Long schoolId);

    @Query("""
        SELECT t FROM Teacher t
        WHERE t.schoolId = :schoolId
          AND t.status = 'ACTIVE'
    """)
    List<Teacher> findActiveTeachersBySchool(@Param("schoolId") Long schoolId);

    @Query("""
        SELECT t FROM Teacher t
        WHERE t.schoolId = :schoolId
          AND t.status = 'ACTIVE'
          AND (
            LOWER(t.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
          )
    """)
    List<Teacher> searchTeachers(
            @Param("schoolId") Long schoolId,
            @Param("search") String search
    );

    @Query("""
        SELECT t FROM Teacher t
        WHERE t.schoolId = :schoolId
          AND t.subjectId = :subjectId
          AND t.status = 'ACTIVE'
    """)
    List<Teacher> filterBySubject(
            @Param("schoolId") Long schoolId,
            @Param("subjectId") Long subjectId
    );

    @Query("""
        SELECT t FROM Teacher t
        WHERE t.id = :teacherId
          AND t.schoolId = :schoolId
    """)
    Teacher findByTeacherIdAndSchoolId(
            @Param("teacherId") Long teacherId,
            @Param("schoolId") Long schoolId
    );
}
