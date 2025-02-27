package education.kub.superadmin.services.inter;

import education.kub.superadmin.entities.AdminEntity;

import java.util.List;

public interface AdminService {
    // create, edit -- NEED DTO
    // resendPassword -- should be handled by Controller
    AdminEntity getAdminById(Long id);
    AdminEntity getAdminByUserId(Long userId);
    List<AdminEntity> getAllAdmin();
    void deleteAdmin(Long id);
}
