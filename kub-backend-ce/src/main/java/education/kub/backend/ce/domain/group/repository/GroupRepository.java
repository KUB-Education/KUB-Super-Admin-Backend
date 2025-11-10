package education.kub.backend.ce.domain.group.repository;

import education.kub.backend.ce.domain.group.entity.GroupEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    @EntityGraph(attributePaths = {"students"})
    Optional<GroupEntity> findGroupWithStudentsById(Long id);
}
