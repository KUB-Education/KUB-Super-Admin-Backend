package education.kub.superadmin.services.impl;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.dto.AdminUpdateRequestDTO;
import education.kub.superadmin.entities.UserEntity;
import education.kub.superadmin.exception.ErrorCode;
import education.kub.superadmin.exception.KubException;
import education.kub.superadmin.repositories.UserRepo;
import education.kub.superadmin.services.inter.PasswordService;
import education.kub.superadmin.services.inter.SmtpService;
import education.kub.superadmin.services.inter.TokenStoreService;
import education.kub.superadmin.services.inter.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final SmtpService smtpService;

    private final PasswordService passwordService;

    private final TokenStoreService tokenStoreService;

    private final String REGISTRATION_EMAIL_SUBJECT = "Registration on KUB Education";
    private final String TEMPORARY_PASSWORD_EMAIL_SUBJECT = "Temporary password for KUB Education";
    private final String TEMPORARY_PASSWORD_EMAIL_BODY_TEMPLATE =
            "Dear %s %s %s\nYour temporary password is: %s (expires in %d days)";
    private final Integer TEMPORARY_PASSWORD_EXPIRATION_DAYS = 7;


    public UserServiceImpl(
            UserRepo userRepo,
            SmtpService smtpService,
            PasswordService passwordService,
            TokenStoreService tokenStoreService
    ) {
        this.userRepo = userRepo;
        this.smtpService = smtpService;
        this.passwordService = passwordService;
        this.tokenStoreService = tokenStoreService;
    }

    @Override
    @Transactional
    public UserEntity createUser(AdminRequestDTO dto) {
        if (userRepo.findByEmail(dto.email()).isPresent()) {
            throw new KubException(ErrorCode.CONFLICT);
        }

        UserEntity user = new UserEntity(
                null,
                dto.lastName(),
                dto.firstName(),
                dto.middleName(),
                dto.email(),
                null,
                null,
                null,
                null
        );
        userRepo.save(user);

        // check SMTP connection
        if (!smtpService.checkConnection()) {
            user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
            userRepo.save(user);
            return user;
        }

        // send temporary password on email
        String temporaryPassword = passwordService.generate();
        boolean sendResult = smtpService.sendEmail(dto.email(), REGISTRATION_EMAIL_SUBJECT,
                genTemporaryPasswordEmailBody(dto.lastName(), dto.firstName(),
                        dto.middleName(), temporaryPassword));

        if (!sendResult) {
            user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
            userRepo.save(user);
            return user;
        }

        // update DB (set temporaryPassword)
        String temporaryPasswordHashed = passwordService.hash(temporaryPassword);
        user.setStatus(UserEntity.Status.ACTIVATION_PENDING);
        user.setTemporaryPasswordHashed(temporaryPasswordHashed);
        user.setTemporaryPasswordExpiration(LocalDateTime.now().plusDays(7));
        userRepo.save(user);
        return user;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new KubException(ErrorCode.NOT_FOUND));
    }

    @Override
    @Transactional
    public UserEntity updateUser(Long id, AdminUpdateRequestDTO dto) {
        UserEntity user = getUserById(id);

        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }
        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }
        if (dto.middleName() != null) {
            if(dto.middleName().isEmpty()){
                user.setMiddleName(null);
            }
            else{
                user.setMiddleName(dto.middleName());
            }
        }

        boolean emailUpdated = false;
        if (dto.email() != null) {
            if (!dto.email().equals(user.getEmail())) {
                user.setEmail(dto.email());
                emailUpdated = true;
            }
        }
        userRepo.save(user);

        if (!emailUpdated) {
            return user;
        }

        if (user.getStatus() == UserEntity.Status.ACTIVATED) {
            tokenStoreService.deleteAllSessions(user.getId());
            return user;
        }

        if (user.getStatus() == UserEntity.Status.RECOVERY_PENDING) {
            tokenStoreService.deleteAllSessions(user.getId());
        }

        user.setStatus(null);
        user.setPasswordHashed(null);
        user.setTemporaryPasswordHashed(null);
        user.setTemporaryPasswordExpiration(null);
        userRepo.save(user);


        // check SMTP connection
        if (!smtpService.checkConnection()) {
            user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
            userRepo.save(user);
            return user;
        }

        // send temporary password on email
        String temporaryPassword = passwordService.generate();
        boolean sendResult = smtpService.sendEmail(user.getEmail(), TEMPORARY_PASSWORD_EMAIL_SUBJECT,
                genTemporaryPasswordEmailBody(user.getLastName(), user.getFirstName(),
                        user.getMiddleName(), temporaryPassword));

        if (!sendResult) {
            user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
            userRepo.save(user);
            return user;
        }

        // update DB (set temporaryPassword)
        String temporaryPasswordHashed = passwordService.hash(temporaryPassword);
        user.setStatus(UserEntity.Status.ACTIVATION_PENDING);
        user.setTemporaryPasswordHashed(temporaryPasswordHashed);
        user.setTemporaryPasswordExpiration(LocalDateTime.now().plusDays(7));
        userRepo.save(user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity user = getUserById(id);

        tokenStoreService.deleteAllSessions(user.getId());

        userRepo.delete(user);
    }

    @Override
    public UserEntity resendTemporaryPassword(Long id) {
        UserEntity user = getUserById(id);

        boolean isUserAlreadyActivated =
                user.getStatus() == UserEntity.Status.ACTIVATED ||
                        user.getStatus() == UserEntity.Status.RECOVERY_PENDING;

        if (!isUserAlreadyActivated) {
            user.setStatus(null);
        }
        user.setTemporaryPasswordHashed(null);
        user.setTemporaryPasswordExpiration(null);
        userRepo.save(user);

        // check SMTP connection
        if (!smtpService.checkConnection()) {
            if (!isUserAlreadyActivated) {
                user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
                userRepo.save(user);
            }
            throw new KubException(ErrorCode.SMTP_FAILURE);
        }

        // send temporary password on email
        String temporaryPassword = passwordService.generate();
        boolean sendResult = smtpService.sendEmail(user.getEmail(), TEMPORARY_PASSWORD_EMAIL_SUBJECT,
                genTemporaryPasswordEmailBody(user.getLastName(), user.getFirstName(),
                        user.getMiddleName(), temporaryPassword));

        if (!sendResult) {
            if (!isUserAlreadyActivated) {
                user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
                userRepo.save(user);
            }
            throw new KubException(ErrorCode.SMTP_FAILURE);
        }

        // update DB (set temporaryPassword)
        String temporaryPasswordHashed = passwordService.hash(temporaryPassword);
        if (!isUserAlreadyActivated) {
            user.setStatus(UserEntity.Status.ACTIVATION_PENDING);
        } else {
            user.setStatus(UserEntity.Status.RECOVERY_PENDING);
        }
        user.setTemporaryPasswordHashed(temporaryPasswordHashed);
        user.setTemporaryPasswordExpiration(LocalDateTime.now().plusDays(7));
        userRepo.save(user);
        return user;
    }

    private String genTemporaryPasswordEmailBody(String lastName, String firstName, String middleName,
                                                 String temporaryPassword) {
        return String.format(TEMPORARY_PASSWORD_EMAIL_BODY_TEMPLATE,
                lastName,
                firstName,
                middleName != null ? middleName : "",
                temporaryPassword,
                TEMPORARY_PASSWORD_EXPIRATION_DAYS
        );
    }
}
