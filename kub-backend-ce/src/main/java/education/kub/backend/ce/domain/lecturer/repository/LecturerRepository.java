package education.kub.backend.ce.domain.lecturer.repository;

import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LecturerRepository extends JpaRepository<LecturerEntity, Long> {
    @EntityGraph(attributePaths = {"user", "lecturerDepartmentPositions", "academicTitles"})
    Optional<LecturerEntity> findFullEntityById(Long id);

    @EntityGraph(attributePaths = {"user", "lecturerDepartmentPositions", "academicTitles"})
    Optional<LecturerEntity> findFullEntityByUserId(Long userId);

    Long user(UserEntity user);
}
