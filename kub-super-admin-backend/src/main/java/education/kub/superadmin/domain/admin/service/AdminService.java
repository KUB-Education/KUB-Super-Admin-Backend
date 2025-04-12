package education.kub.superadmin.domain.admin.service;

import education.kub.superadmin.domain.admin.model.AdminRequestDTO;
import education.kub.superadmin.domain.admin.model.AdminResponseDTO;
import education.kub.superadmin.domain.admin.model.AdminUpdateRequestDTO;

import java.util.List;

public interface AdminService {
    AdminResponseDTO createAdmin(AdminRequestDTO dto);

    List<AdminResponseDTO> getAdmins();

    AdminResponseDTO getAdmin(Long id);

    AdminResponseDTO updateAdmin(Long id, AdminUpdateRequestDTO dto);

    void deleteAdmin(Long id);

    AdminResponseDTO resendAdminPassword(Long id);
}
