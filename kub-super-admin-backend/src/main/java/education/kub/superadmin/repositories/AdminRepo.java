package education.kub.superadmin.repositories;

import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<AdminEntity, Long> {
    Optional<AdminEntity> findByUserId(Long userId);
}
