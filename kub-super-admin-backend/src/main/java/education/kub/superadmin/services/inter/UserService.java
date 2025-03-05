package education.kub.superadmin.services.inter;

import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;

import java.util.List;

public interface UserService {
    // create, edit -- NEED DTO
    // resendPassword -- should be handled by Controller
    UserEntity getUserById(Long id);
    List<UserEntity> getAllUsers();
    void deleteUser(Long id);
}
