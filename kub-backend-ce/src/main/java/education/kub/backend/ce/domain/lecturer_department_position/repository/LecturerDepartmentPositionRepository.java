package education.kub.backend.ce.domain.lecturer_department_position.repository;

import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LecturerDepartmentPositionRepository
        extends JpaRepository<LecturerDepartmentPositionEntity, Long> {
    boolean existsByLecturerIdAndDepartmentId(Long lecturerId, Long departmentId);

    boolean existsByIdAndLecturerId(Long id, Long lecturerId);

    Optional<LecturerDepartmentPositionEntity> findByLecturerIdAndDepartmentId(Long lecturerId, Long departmentId);
}