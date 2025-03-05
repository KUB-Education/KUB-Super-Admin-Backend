package education.kub.superadmin.services.inter;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserService {
    // edit -- NEED DTO
    // resendPassword -- should be handled by Controller

    /**
     * Creates User entity. Also, if connection to SMTP server is ok,
     * generates temporary password and sends it to <code>dto.email</code>
     * @param dto
     * @return created entity
     * @throws <code>EntityExistsException</code> when user with given email already exists
     */
    UserEntity createUser(AdminRequestDTO dto);


    UserEntity getUserById(Long id);
    List<UserEntity> getAllUsers();
    void deleteUser(Long id);
}
