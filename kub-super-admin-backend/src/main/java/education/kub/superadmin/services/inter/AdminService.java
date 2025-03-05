package education.kub.superadmin.services.inter;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.entities.AdminEntity;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface AdminService {
    // edit -- NEED DTO
    // resendPassword -- should be handled by Controller

    /**
     * Creates Admin entity and corresponding User entity. Also, if connection to SMTP server is ok,
     * generates temporary password and sends it to <code>dto.email</code>
     * @param dto
     * @return created entity
     * @throws <code>EntityExistsException</code> when user with given email already exists
     */
    AdminEntity createAdmin(AdminRequestDTO dto);

    AdminEntity getAdminById(Long id);
    AdminEntity getAdminByUserId(Long userId);
    List<AdminEntity> getAllAdmin();
    void deleteAdmin(Long id);
}
