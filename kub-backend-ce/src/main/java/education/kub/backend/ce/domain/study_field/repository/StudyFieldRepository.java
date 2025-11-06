package education.kub.backend.ce.domain.study_field.repository;

import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudyFieldRepository extends JpaRepository<StudyFieldEntity, Long> {
    boolean existsByCode(String code);

    boolean existsByCodeAndIdIsNot(String code, Long id);

    List<StudyFieldEntity> findByNameContainingIgnoreCase(String nameSubstring);
}
