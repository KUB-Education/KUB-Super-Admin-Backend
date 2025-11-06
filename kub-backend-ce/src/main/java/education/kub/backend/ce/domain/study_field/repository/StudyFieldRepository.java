package education.kub.backend.ce.domain.study_field.repository;

import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyFieldRepository extends JpaRepository<StudyFieldEntity, Long> {
}
