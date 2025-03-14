package education.kub.superadmin.services.inter;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.dto.AdminUpdateRequestDTO;
import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;
import education.kub.superadmin.exception.KubException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import javax.naming.ServiceUnavailableException;
import java.util.List;

public interface AdminService {

    /**
     * Creates Admin entity and corresponding User entity. Also, if connection to SMTP server is ok,
     * generates temporary password and sends it to <code>dto.email</code>
     * @param dto
     * @return created entity
     * @throws KubException (<code>ErrorCode.CONFLICT</code>) - when user with given email already exists
     */
    AdminEntity createAdmin(AdminRequestDTO dto);

    /**
     * Updates Admin entity according to given <code>dto</code>.
     * <br/>
     * Do the same as a {@link UserService#updateUser(Long, AdminUpdateRequestDTO)} method.
     *
     * @param id ID of Admin
     * @param dto
     * @return updated entity
     * @throws KubException (<code>ErrorCode.NOT_FOUND</code>) - when admin with given <code>id</code> does not exist
     */
    AdminEntity updateAdmin(Long id, AdminUpdateRequestDTO dto);

    /**
     * Resends temporary password to admin with given <code>id</code>.
     * @param id
     * @return
     * @throws KubException (<code>ErrorCode.SMTP_FAILURE</code>) - if SMTP is not available, or if sending email is failed;
     * (<code>ErrorCode.NOT_FOUND</code>) - when admin with given <code>id</code> does not exist
     */
    AdminEntity resendTemporaryPassword(Long id);

    /**
     * Finds admin by given <code>id</code>
     * @param id
     * @throws KubException (<code>ErrorCode.NOT_FOUND</code>) - when admin with given <code>id</code> does not exist
     */
    AdminEntity getAdminById(Long id);

    /**
     * Finds admin by given <code>userId</code>
     * @param userId
     * @throws KubException (<code>ErrorCode.NOT_FOUND</code>) - when admin with given <code>userId</code> does not exist
     */
    AdminEntity getAdminByUserId(Long userId);

    /**
     * Finds all admins
     * @return
     */
    List<AdminEntity> getAllAdmins();

    /**
     * Deletes admin by given <code>id</code>
     * @param id
     * @throws KubException (<code>ErrorCode.NOT_FOUND</code>) - when admin with given <code>id</code> does not exist
     */
    void deleteAdmin(Long id);
}
