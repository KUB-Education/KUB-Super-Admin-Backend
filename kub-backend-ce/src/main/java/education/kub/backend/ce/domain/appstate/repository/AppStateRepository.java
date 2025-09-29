package education.kub.backend.ce.domain.appstate.repository;

import education.kub.backend.ce.domain.appstate.entity.AppStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppStateRepository extends JpaRepository<AppStateEntity, Long> {
    Optional<AppStateEntity> findByKey(String key);
}
