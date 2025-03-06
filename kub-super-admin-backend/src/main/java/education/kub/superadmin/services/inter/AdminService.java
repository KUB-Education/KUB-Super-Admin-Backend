package education.kub.superadmin.services.inter;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.dto.AdminUpdateRequestDTO;
import education.kub.superadmin.entities.AdminEntity;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface AdminService {
    // resendPassword -- should be handled by Controller

    /**
     * Creates Admin entity and corresponding User entity. Also, if connection to SMTP server is ok,
     * generates temporary password and sends it to <code>dto.email</code>
     * @param dto
     * @return created entity
     * @throws <code>EntityExistsException</code> when user with given email already exists
     */
    AdminEntity createAdmin(AdminRequestDTO dto);

    /**
     * Updates Admin entity according to given <code>dto</code> (only non-null fields are considered).
     * <br/>
     * Do the same as a {@link UserService#updateUser(Long, AdminUpdateRequestDTO)} method.
     *
     * @param id ID of Admin
     * @param dto
     * @return updated entity
     * @throws <code>EntityNotFoundException</code> when admin with given <code>id</code> does not exist
     */
    AdminEntity updateAdmin(Long id, AdminUpdateRequestDTO dto);

    AdminEntity getAdminById(Long id);
    AdminEntity getAdminByUserId(Long userId);
    List<AdminEntity> getAllAdmins();
    void deleteAdmin(Long id);
}
