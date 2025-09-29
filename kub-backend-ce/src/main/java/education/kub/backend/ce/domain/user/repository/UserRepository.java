package education.kub.backend.ce.domain.user.repository;

import education.kub.backend.ce.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByIdAndDeletedAtIsNull(Long id);

    @EntityGraph(attributePaths = "roles")
    Optional<UserEntity> findWithRolesById(Long id);

    boolean existsByEmailAndDeletedAtIsNull(String email);

    Optional<UserEntity> findByEmailAndDeletedAtIsNull(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<UserEntity> findWithRolesByEmailAndDeletedAtIsNull(String email);

    List<UserEntity> findAllByDeletedAtIsNull();
}
