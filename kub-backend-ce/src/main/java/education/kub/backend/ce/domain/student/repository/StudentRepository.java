package education.kub.backend.ce.domain.student.repository;

import education.kub.backend.ce.domain.student.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}
