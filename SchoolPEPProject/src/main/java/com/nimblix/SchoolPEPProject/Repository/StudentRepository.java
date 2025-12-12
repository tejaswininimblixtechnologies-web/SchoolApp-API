package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {


    // ✅ Default: All students by schoolId
    List<Student> findBySchoolId(Long schoolId);

    // ✅ Filter by school + class
    @Query("SELECT s FROM student s WHERE s.schoolId = :schoolId AND s.classId = :classId")
    List<Student> findBySchoolAndClass(
            @Param("schoolId") Long schoolId,
            @Param("classId") Long classId
    );

    // ✅ Filter by school + status
    @Query("SELECT s FROM student s WHERE s.schoolId = :schoolId AND s.status = :status")
    List<Student> findBySchoolAndStatus(
            @Param("schoolId") Long schoolId,
            @Param("status") String status
    );

    // ✅ Filter by school + section
    @Query("SELECT s FROM student s WHERE s.schoolId = :schoolId AND s.section = :section")
    List<Student> findBySchoolAndSection(
            @Param("schoolId") Long schoolId,
            @Param("section") String section
    );

    @Query("""
    SELECT s FROM student s
    WHERE s.schoolId = :schoolId
      AND (:classId IS NULL OR s.classId = :classId)
      AND (:section IS NULL OR s.section = :section)
      AND (:status IS NULL OR s.status = :status)
""")
    List<Student> findByAllFilters(
            @Param("schoolId") Long schoolId,
            @Param("classId") Long classId,
            @Param("section") String section,
            @Param("status") String status
    );

}
