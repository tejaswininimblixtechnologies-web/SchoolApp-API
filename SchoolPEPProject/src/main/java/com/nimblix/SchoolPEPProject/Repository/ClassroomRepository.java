package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
    List<Classroom> findByClassroomNameAndSchoolId(String classroomName, Long schoolId);

    boolean existsByIdAndSchoolId(Long id, Long schoolId);

    Optional<Object> findByIdAndSchoolId(Long classRoomId, Long schoolId);

    @Query("SELECT c FROM Classroom c WHERE c.teacherId = :teacherId")
    List<Classroom> findByTeacherId(@Param("teacherId") String teacherId);

    @Query("SELECT c FROM Classroom c WHERE c.id IN :classRoomIds")
    List<Classroom> findByIdIn(@Param("classRoomIds") List<Long> classRoomIds);
}
