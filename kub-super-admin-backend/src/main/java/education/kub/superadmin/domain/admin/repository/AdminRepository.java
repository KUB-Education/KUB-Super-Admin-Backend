package education.kub.superadmin.domain.admin.repository;

import education.kub.superadmin.domain.admin.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    Optional<AdminEntity> findByUserId(Long userId);
}
