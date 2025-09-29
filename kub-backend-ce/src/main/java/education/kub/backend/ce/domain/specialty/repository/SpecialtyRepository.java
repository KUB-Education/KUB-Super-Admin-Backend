package education.kub.backend.ce.domain.specialty.repository;

import education.kub.backend.ce.domain.specialty.domain.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, Long> {
}
