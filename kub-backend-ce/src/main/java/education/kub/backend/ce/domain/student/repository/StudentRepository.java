package education.kub.backend.ce.domain.student.repository;

import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.student.entity.StudentEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    @EntityGraph(attributePaths = {"user", "studentEducationalPrograms", "groups"})
    Optional<StudentEntity> findFullEntityById(Long id);

    @EntityGraph(attributePaths = {"user", "studentEducationalPrograms", "groups"})
    Optional<StudentEntity> findFullEntityByUserId(Long userId);
}
