package education.kub.superadmin.repositories;

import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<AdminEntity, Long> {
}
