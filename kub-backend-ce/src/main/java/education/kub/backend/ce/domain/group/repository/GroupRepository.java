package education.kub.backend.ce.domain.group.repository;

import education.kub.backend.ce.domain.group.domain.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
}
