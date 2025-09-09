package education.kub.backend.ce.domain.academic_title.repository;

import education.kub.backend.ce.domain.academic_title.entity.AcademicTitleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicTitleRepository extends JpaRepository<AcademicTitleEntity, Long> {
}
