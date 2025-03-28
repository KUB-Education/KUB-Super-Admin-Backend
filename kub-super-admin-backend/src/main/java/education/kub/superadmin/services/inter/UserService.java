package education.kub.superadmin.services.inter;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.dto.AdminUpdateRequestDTO;
import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;
import education.kub.superadmin.exception.ErrorCode;
import education.kub.superadmin.exception.KubException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import javax.naming.ServiceUnavailableException;
import java.util.List;

public interface UserService {
    /**
     * Creates User entity. Also, if connection to SMTP server is ok,
     * generates temporary password and sends it to <code>dto.email</code>
     * @param dto
     * @return created entity
     * @throws KubException (<code>ErrorCode.CONFLICT</code>) - when user with given email already exists
     */
    UserEntity createUser(AdminRequestDTO dto);

    /**
     * Finds all users
     * @return
     */
    List<UserEntity> getAllUsers();

    /**
     * Finds user by given <code>id</code>
     * @param id
     * @return
     * @throws KubException (<code>ErrorCode.NOT_FOUND</code>) - when user with given <code>id</code> does not exist
     */
    UserEntity getUserById(Long id);

    /**
     * Updates User entity according to given <code>dto</code> (only non-null fields are considered).
     * <br/>
     * If <code>dto.middleName</code> is empty string, then middleName of user will be set to <code>null</code>.
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
     * @throws KubException (<code>ErrorCode.NOT_FOUND</code>) - when user with given <code>id</code> does not exist
     */
    UserEntity updateUser(Long id, AdminUpdateRequestDTO dto);

    /**
     * Deletes user with given <code>id</code>
     * @param id
     * @throws KubException (<code>ErrorCode.NOT_FOUND</code>) - when user with given <code>id</code> does not exist
     */
    void deleteUser(Long id);

    /**
     * Resends temporary password to user with given <code>id</code>.
     * @param id
     * @return
     * @throws KubException (<code>ErrorCode.SMTP_FAILURE</code>) - if SMTP is not available, or sending email is failed;
     * (<code>ErrorCode.NOT_FOUND</code>) - when user with given <code>id</code> does not exist
     */
    UserEntity resendTemporaryPassword(Long id);
}
