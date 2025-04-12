package education.kub.superadmin.domain.user.service;

import education.kub.superadmin.app.properties.AppAccountRegistrationProperties;
import education.kub.superadmin.domain.user.model.UserResponseDTO;
import education.kub.superadmin.infrastructure.password.service.PasswordService;
import education.kub.superadmin.infrastructure.token.store.service.TokenStoreService;
import education.kub.superadmin.domain.user.model.UserRequestDTO;
import education.kub.superadmin.infrastructure.email.service.EmailService;
import education.kub.superadmin.domain.user.entity.UserEntity;
import education.kub.superadmin.app.exception.model.KubException;
import education.kub.superadmin.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final EmailService emailService;

    private final PasswordService passwordService;

    private final TokenStoreService tokenStoreService;

    private final AppAccountRegistrationProperties appAccountRegistrationProperties;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.email()).isPresent()) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        UserEntity userEntity = new UserEntity(
                null,
                userRequestDTO.lastName(),
                userRequestDTO.firstName(),
                userRequestDTO.middleName(),
                userRequestDTO.email(),
                null,
                null,
                null,
                null
        );
        if (userRequestDTO.middleName() != null && userRequestDTO.middleName().isEmpty()) {
            userEntity.setMiddleName(null);
        }
        userRepository.save(userEntity);

        return resendUserPassword(userEntity.getId());
    }

    @Override
    public List<UserResponseDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(UserEntity::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND)).toUserResponseDTO();
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        boolean emailUpdated = false;
        if (userRequestDTO.lastName() != null) {
            userEntity.setLastName(userRequestDTO.lastName());
        }
        if (userRequestDTO.firstName() != null) {
            userEntity.setFirstName(userRequestDTO.firstName());
        }
        if (userRequestDTO.middleName() != null) {
            if (userRequestDTO.middleName().isEmpty()) {
                userEntity.setMiddleName(null);
            } else {
                userEntity.setMiddleName(userRequestDTO.middleName());
            }
        }
        if (userRequestDTO.email() != null) {
            if (!userRequestDTO.email().equals(userEntity.getEmail())) {
                userEntity.setEmail(userRequestDTO.email());
                emailUpdated = true;
            }
        }
        if (!emailUpdated) {
            userRepository.save(userEntity);

            return userEntity.toUserResponseDTO();
        } else {
            userEntity.setStatus(null);
            userEntity.setPasswordHashed(null);
            userEntity.setTemporaryPasswordHashed(null);
            userEntity.setTemporaryPasswordExpiration(null);

            userRepository.save(userEntity);
        }

        if (userEntity.getStatus() == UserEntity.Status.ACTIVATED || userEntity.getStatus() == UserEntity.Status.RECOVERY_PENDING) {
            tokenStoreService.deleteAllSessions(userEntity.getId());
        }

        return resendUserPassword(userEntity.getId());
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        tokenStoreService.deleteAllSessions(userEntity.getId());

        userRepository.delete(userEntity);
    }

    @Override
    public UserResponseDTO resendUserPassword(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        userEntity.setStatus(null);
        userEntity.setPasswordHashed(null);
        userEntity.setTemporaryPasswordHashed(null);
        userEntity.setTemporaryPasswordExpiration(null);

        String temporaryPassword = passwordService.generate();

        boolean emailSent = emailService.sendRegistrationEmail(
                new UserRequestDTO(
                        userEntity.getLastName(),
                        userEntity.getFirstName(),
                        userEntity.getMiddleName(),
                        userEntity.getEmail()
                ),
                temporaryPassword
        );

        if (!emailSent) {
            userEntity.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
        } else {
            String temporaryPasswordHashed = passwordService.hash(temporaryPassword);

            userEntity.setStatus(UserEntity.Status.ACTIVATION_PENDING);
            userEntity.setTemporaryPasswordHashed(temporaryPasswordHashed);
            userEntity.setTemporaryPasswordExpiration(
                    LocalDateTime.now().plusDays(appAccountRegistrationProperties.temporaryPasswordExpirationDays())
            );
        }

        userRepository.save(userEntity);

        return userEntity.toUserResponseDTO();
    }
}
