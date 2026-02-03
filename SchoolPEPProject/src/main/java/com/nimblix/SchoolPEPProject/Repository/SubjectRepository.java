package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subjects,Long> {
    boolean existsByIdAndClassRoomId(Long id, Long classRoomId);

    Optional<Subjects> findBySubjectNameAndClassRoomIdAndTeacher_Id(
            String subjectName,
            Long classRoomId,
            Long teacherId
    );

    @Query("SELECT s FROM Subjects s WHERE s.teacher.id = :teacherId")
    List<Subjects> findByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT DISTINCT s.classRoomId FROM Subjects s WHERE s.teacher.id = :teacherId")
    List<Long> findDistinctClassRoomIdsByTeacherId(@Param("teacherId") Long teacherId);
}
