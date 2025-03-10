package education.kub.superadmin.services.impl;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.dto.AdminUpdateRequestDTO;
import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;
import education.kub.superadmin.exception.ErrorCode;
import education.kub.superadmin.exception.KubException;
import education.kub.superadmin.repositories.AdminRepo;
import education.kub.superadmin.repositories.UserRepo;
import education.kub.superadmin.services.inter.AdminService;
import education.kub.superadmin.services.inter.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepo adminRepo;
    private final UserRepo userRepo;
    private final UserService userService;


    public AdminServiceImpl(AdminRepo adminRepo, UserRepo userRepo,
                            UserService userService) {
        this.adminRepo = adminRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @Override
    @Transactional
    public AdminEntity createAdmin(AdminRequestDTO dto){
        UserEntity user = userService.createUser(dto);

        AdminEntity admin = new AdminEntity(null, user);
        adminRepo.save(admin);

        return admin;
    }

    @Override
    @Transactional
    public AdminEntity updateAdmin(Long id, AdminUpdateRequestDTO dto) {
        AdminEntity admin = getAdminById(id);

        userService.updateUser(admin.getUser().getId(), dto);

        // refresh, because admin.user is changed
        return getAdminById(id);
    }

    @Override
    public AdminEntity resendTemporaryPassword(Long id) throws ServiceUnavailableException {
        AdminEntity admin = getAdminById(id);

        userService.resendTemporaryPassword(admin.getUser().getId());

        // refresh, because admin.user is changed
        return getAdminById(id);
    }

    @Override
    public AdminEntity getAdminById(Long id) {
        return adminRepo.findById(id).orElseThrow(() -> new KubException(ErrorCode.NOT_FOUND));
    }

    @Override
    public AdminEntity getAdminByUserId(Long userId) {
        return adminRepo.findByUserId(userId).orElseThrow(() -> new KubException(ErrorCode.NOT_FOUND));
    }

    @Override
    public List<AdminEntity> getAllAdmins() {
        return adminRepo.findAll();
    }

    @Override
    public void deleteAdmin(Long id) {
        AdminEntity admin = getAdminById(id);
        UserEntity user = admin.getUser();

        adminRepo.delete(admin);
        userRepo.delete(user);
    }
}
