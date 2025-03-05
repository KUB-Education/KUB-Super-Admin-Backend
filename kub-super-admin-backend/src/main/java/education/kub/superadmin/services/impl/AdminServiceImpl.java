package education.kub.superadmin.services.impl;

import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;
import education.kub.superadmin.repositories.AdminRepo;
import education.kub.superadmin.repositories.UserRepo;
import education.kub.superadmin.services.inter.AdminService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final AdminRepo adminRepo;
    private final UserRepo userRepo;

    public AdminServiceImpl(AdminRepo adminRepo, UserRepo userRepo) {
        this.adminRepo = adminRepo;
        this.userRepo = userRepo;
    }

    @Override
    public AdminEntity getAdminById(Long id) {
        AdminEntity admin = adminRepo.findById(id).orElse(null);
        if(admin == null){
            throw new EntityNotFoundException(String.format("Admin with id=%d does not exist", id));
        }

        return admin;
    }

    @Override
    public AdminEntity getAdminByUserId(Long userId) {
        AdminEntity admin = adminRepo.findByUserId(userId).orElse(null);
        if(admin == null){
            throw new EntityNotFoundException(String.format("Admin with userId=%d does not exist", userId));
        }

        return admin;
    }

    @Override
    public List<AdminEntity> getAllAdmin() {
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
