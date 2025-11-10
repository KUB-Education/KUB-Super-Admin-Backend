package education.kub.backend.ce.domain.specialty.repository;

import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, Long> {
    List<SpecialtyEntity> findByStudyFieldId(Long studyFieldId);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdIsNot(String code, Long id);
}
