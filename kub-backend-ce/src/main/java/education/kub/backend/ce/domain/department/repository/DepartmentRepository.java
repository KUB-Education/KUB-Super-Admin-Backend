package education.kub.backend.ce.domain.department.repository;

import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    boolean existsByName(String name);
    List<DepartmentEntity> findByNameContainingIgnoreCase(String nameSubstring);
}