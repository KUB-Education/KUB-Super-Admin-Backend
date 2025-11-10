package education.kub.backend.ce.domain.user.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.app.properties.AppAccountRegistrationProperties;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.mapper.UserMapper;
import education.kub.backend.ce.domain.user.model.*;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.infrastructure.email.service.EmailService;
import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserRoleService userRoleService;

    private final UserMapper userMapper;

    private final EmailService emailService;

    private final PasswordService passwordService;

    private final TokenStoreService tokenStoreService;

    private final AppAccountRegistrationProperties appAccountRegistrationProperties;

    @Transactional
    public UserDetailsResponse createUser(UserCreateRequest userCreateRequest) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(userCreateRequest.email())) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        var user = new UserEntity();
        user.setLastName(userCreateRequest.lastName());
        user.setFirstName(userCreateRequest.firstName());
        user.setMiddleName(userCreateRequest.middleName());
        user.setEmail(userCreateRequest.email());

        if (userCreateRequest.middleName() != null && userCreateRequest.middleName().isBlank()) {
            user.setMiddleName(null);
        }
        userRepository.save(user);

        userRoleService.addUserRoleByType(user.getId(), RoleEntity.Type.USER);

        return sendUserPassword(user.getId());
    }

    public UserDetailsResponse sendUserPassword(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        user.setStatus(null);
        user.setPasswordHashed(null);
        user.setTemporaryPasswordHashed(null);
        user.setTemporaryPasswordExpiresAt(null);

        var temporaryPassword = passwordService.generate();

        boolean emailSent = emailService.sendRegistrationEmail(
                userMapper.toRegistrationData(user),
                temporaryPassword
        );

        if (!emailSent) {
            user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
        } else {
            var temporaryPasswordHashed = passwordService.hash(temporaryPassword);

            user.setStatus(UserEntity.Status.ACTIVATION_PENDING);
            user.setTemporaryPasswordHashed(temporaryPasswordHashed);
            user.setTemporaryPasswordExpiresAt(
                    Instant.now().plus(
                            appAccountRegistrationProperties.temporaryPasswordExpirationDays(),
                            ChronoUnit.DAYS
                    )
            );
        }

        userRepository.save(user);

        return userMapper.toDetailsResponse(user);
    }

    public List<UserDetailsResponse> getUsers() {
        return userMapper.toDetailsResponseList(userRepository.findAllByDeletedAtIsNull());
    }

    public UserDetailsResponse getUserWithRoles(Long id) {
        return userMapper.toDetailsResponse(userRepository.findWithRolesById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND)));
    }

    @Transactional
    public UserDetailsResponse updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        boolean emailUpdated = false;

        if (userUpdateRequest.lastName() != null) {
            user.setLastName(userUpdateRequest.lastName());
        }

        if (userUpdateRequest.firstName() != null) {
            user.setFirstName(userUpdateRequest.firstName());
        }

        if (userUpdateRequest.middleName() != null) {
            if (userUpdateRequest.middleName().isBlank()) {
                user.setMiddleName(null);
            } else {
                user.setMiddleName(userUpdateRequest.middleName());
            }
        }

        if (userUpdateRequest.email() != null) {
            if (userRepository.existsByEmailAndDeletedAtIsNull(userUpdateRequest.email())) {
                throw new KubException(KubException.ErrorCode.CONFLICT);
            }

            if (!userUpdateRequest.email().equals(user.getEmail())) {
                user.setEmail(userUpdateRequest.email());
                emailUpdated = true;
            }
        }

        if (!emailUpdated) {
            userRepository.save(user);

            return userMapper.toDetailsResponse(user);
        } else {
            user.setStatus(null);
            user.setPasswordHashed(null);
            user.setTemporaryPasswordHashed(null);
            user.setTemporaryPasswordExpiresAt(null);

            userRepository.save(user);
        }

        if (user.getStatus() == UserEntity.Status.ACTIVATED || user.getStatus() == UserEntity.Status.RECOVERY_PENDING) {
            tokenStoreService.deleteAllSessions(user.getId());
        }

        return sendUserPassword(user.getId());
    }

    public void deleteUser(Long id) {
        var user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        tokenStoreService.deleteAllSessions(user.getId());

        user.setDeletedAt(Instant.now());

        userRepository.save(user);
    }

    public void changeUserPassword(Long id, UserPasswordChangeRequest userPasswordChangeRequest) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        if (!passwordService.matches(userPasswordChangeRequest.oldPassword(), user.getPasswordHashed())) {
            throw new KubException(KubException.ErrorCode.UNAUTHORIZED);
        }

        String newPasswordHashed = passwordService.hash(userPasswordChangeRequest.newPassword());
        user.setPasswordHashed(newPasswordHashed);
        userRepository.save(user);

        tokenStoreService.deleteAllSessions(user.getId());
    }

    public void recoveryUserPassword(UserPasswordRecoveryRequest userPasswordRecoveryRequest) {
        var user = userRepository.findByEmailAndDeletedAtIsNull(userPasswordRecoveryRequest.email())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        sendUserPassword(user.getId());
    }
}
