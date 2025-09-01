package education.kub.backend.ce.domain.lecturer.repository;

import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepository extends JpaRepository<LecturerEntity, Long> {
}
