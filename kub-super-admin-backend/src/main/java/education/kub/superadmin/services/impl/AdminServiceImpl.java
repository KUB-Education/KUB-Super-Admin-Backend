package education.kub.superadmin.services.impl;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;
import education.kub.superadmin.generators.password.IPasswordGenerator;
import education.kub.superadmin.generators.password.impl.PasswordGeneratorImpl;
import education.kub.superadmin.helpers.hasher.IHasher;
import education.kub.superadmin.helpers.hasher.impl.extend.BCryptPasswordHasher;
import education.kub.superadmin.repositories.AdminRepo;
import education.kub.superadmin.repositories.UserRepo;
import education.kub.superadmin.services.SmtpService;
import education.kub.superadmin.services.inter.AdminService;
import education.kub.superadmin.services.inter.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
