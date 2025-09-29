package education.kub.backend.ce.domain.department_lecturer.repository;

import education.kub.backend.ce.domain.department_lecturer.entity.DepartmentLecturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentLecturerRepository extends JpaRepository<DepartmentLecturerEntity, Long> {
}
