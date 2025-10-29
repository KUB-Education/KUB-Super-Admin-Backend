package education.kub.backend.ce.domain.specialty.repository;

import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, Long> {
    boolean existsByName(String name);

    boolean existsByCode(String code);
}
