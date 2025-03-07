package education.kub.superadmin.services.inter;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.dto.AdminUpdateRequestDTO;
import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import javax.naming.ServiceUnavailableException;
import java.util.List;

public interface UserService {
    // resendPassword -- should be handled by Controller

    /**
     * Creates User entity. Also, if connection to SMTP server is ok,
     * generates temporary password and sends it to <code>dto.email</code>
     * @param dto
     * @return created entity
     * @throws EntityExistsException when user with given email already exists
     */
    UserEntity createUser(AdminRequestDTO dto);

    /**
     * Updates User entity according to given <code>dto</code> (only non-null fields are considered).
     * <ol>
     * If email is updated, then:
     *     <li>deletes all tokens from Redis (if such exists)</li>
     *     <li>if user status is not <code>ACTIVATED</code> and if connection to SMTP server is ok,
     *     generates new temporary password and sends email with it to <code>dto.email</code></li>
     * </ol>
     *
     * @param id
     * @param dto
     * @return updated entity
     * @throws EntityNotFoundException when user with given <code>id</code> does not exist
     */
    UserEntity updateUser(Long id, AdminUpdateRequestDTO dto);

    /**
     * Resends temporary password to user with given <code>id</code>.
     * @param id
     * @return
     * @throws ServiceUnavailableException if SMTP is not available, or sending email is failed
     * @throws EntityNotFoundException when user with given <code>id</code> does not exist
     */
    UserEntity resendTemporaryPassword(Long id) throws ServiceUnavailableException;

    UserEntity getUserById(Long id);
    List<UserEntity> getAllUsers();
    void deleteUser(Long id);
}
