package education.kub.backend.ce.domain.user.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.app.properties.AppAccountRegistrationProperties;
import education.kub.backend.ce.domain.role.repository.RoleRepository;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.mapper.UserMapper;
import education.kub.backend.ce.domain.user.model.UserCreateRequest;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.infrastructure.email.service.EmailService;
import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

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

    public UserDetailsResponse addRole(Long id, String name) {
        var user = userRepository.findWithRolesById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        var role = roleRepository.findByName(name)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        user.addRole(role);

        userRepository.save(user);

        return userMapper.toDetailsResponse(user);
    }
}
