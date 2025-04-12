package education.kub.superadmin.domain.admin.service;

import education.kub.superadmin.domain.admin.model.AdminRequestDTO;
import education.kub.superadmin.domain.admin.model.AdminResponseDTO;
import education.kub.superadmin.domain.admin.model.AdminUpdateRequestDTO;
import education.kub.superadmin.domain.admin.entity.AdminEntity;
import education.kub.superadmin.app.exception.model.KubException;
import education.kub.superadmin.domain.admin.repository.AdminRepository;
import education.kub.superadmin.domain.user.entity.UserEntity;
import education.kub.superadmin.domain.user.model.UserRequestDTO;
import education.kub.superadmin.domain.user.model.UserResponseDTO;
import education.kub.superadmin.domain.user.reader.UserReader;
import education.kub.superadmin.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    private final UserService userService;

    private final UserReader userReader;

    @Override
    @Transactional
    public AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(
                new UserRequestDTO(
                        adminRequestDTO.lastName(),
                        adminRequestDTO.firstName(),
                        adminRequestDTO.middleName(),
                        adminRequestDTO.email()
                )
        );

        AdminEntity adminEntity = new AdminEntity(null, userReader.getUser(userResponseDTO.id()));

        adminRepository.save(adminEntity);

        return adminEntity.toAdminResponseDto();
    }

    @Override
    public List<AdminResponseDTO> getAdmins() {
        List<AdminEntity> adminEntities = adminRepository.findAll();

        return adminEntities.stream()
                .map(AdminEntity::toAdminResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdminResponseDTO getAdmin(Long id) {
        AdminEntity adminEntity = adminRepository.findById(id).orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return adminEntity.toAdminResponseDto();
    }

    @Override
    @Transactional
    public AdminResponseDTO updateAdmin(Long id, AdminUpdateRequestDTO adminUpdateRequestDTO) {
        AdminEntity adminEntity = adminRepository.findById(id).orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        userService.updateUser(
                adminEntity.getUser().getId(),
                new UserRequestDTO(
                        adminUpdateRequestDTO.lastName(),
                        adminUpdateRequestDTO.firstName(),
                        adminUpdateRequestDTO.middleName(),
                        adminUpdateRequestDTO.email()
                )
        );

        return getAdmin(id);
    }

    @Override
    public void deleteAdmin(Long id) {
        AdminEntity adminEntity = adminRepository.findById(id).orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        Long userId = adminEntity.getUser().getId();

        adminRepository.delete(adminEntity);
        userService.deleteUser(userId);
    }

    @Override
    public AdminResponseDTO resendAdminPassword(Long id) {
        AdminResponseDTO adminResponseDTO = getAdmin(id);

        userService.resendUserPassword(adminResponseDTO.userId());

        return getAdmin(id);
    }
}
