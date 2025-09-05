package education.kub.backend.ce.domain.department.repository;

import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
}
