package education.kub.backend.ce.domain.lecturer_department_position.repository;

import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerDepartmentPositionRepository
        extends JpaRepository<LecturerDepartmentPositionEntity, Long> {
}
