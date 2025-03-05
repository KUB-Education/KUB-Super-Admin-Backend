package education.kub.superadmin.repositories;

import education.kub.superadmin.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<UserEntity, Long> {
}
