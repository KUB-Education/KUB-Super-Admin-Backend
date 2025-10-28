package education.kub.backend.ce.domain.position.repository;

import education.kub.backend.ce.domain.position.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {
    boolean existsByName(PositionEntity.PositionName positionName);
}
