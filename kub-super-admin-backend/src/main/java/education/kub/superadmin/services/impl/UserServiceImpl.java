package education.kub.superadmin.services.impl;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.dto.AdminUpdateRequestDTO;
import education.kub.superadmin.entities.UserEntity;
import education.kub.superadmin.generators.password.IPasswordGenerator;
import education.kub.superadmin.generators.password.impl.PasswordGeneratorImpl;
import education.kub.superadmin.helpers.hasher.IHasher;
import education.kub.superadmin.helpers.hasher.impl.extend.BCryptPasswordHasher;
import education.kub.superadmin.repositories.UserRepo;
import education.kub.superadmin.services.SmtpService;
import education.kub.superadmin.services.inter.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final SmtpService smtpService;

    private final IPasswordGenerator passwordGenerator = new PasswordGeneratorImpl();
    private final IHasher hasher = new BCryptPasswordHasher();

    private final String REGISTRATION_EMAIL_SUBJECT = "Registration on KUB Education";
    private final String TEMPORARY_PASSWORD_EMAIL_SUBJECT = "Temporary password for KUB Education";
    private final String TEMPORARY_PASSWORD_EMAIL_BODY_TEMPLATE =
            "Dear %s %s %s\nYour temporary password is: %s (expires in %d days)";
    private final Integer TEMPORARY_PASSWORD_EXPIRATION_DAYS = 7;


    public UserServiceImpl(UserRepo userRepo, SmtpService smtpService) {
        this.userRepo = userRepo;
        this.smtpService = smtpService;
    }

    @Override
    @Transactional
    public UserEntity createUser(AdminRequestDTO dto){
        if(userRepo.findByEmail(dto.getEmail()).isPresent()){
            throw new EntityExistsException(
                    String.format("User with email '%s' already exists", dto.getEmail()));
        }

        UserEntity user = new UserEntity(
                null,
                dto.getLastName(),
                dto.getFirstName(),
                dto.getMiddleName(),
                dto.getEmail(),
                null, null, null, null);
        userRepo.save(user);

        // check SMTP connection
        if(!smtpService.checkConnection()){
            user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
            userRepo.save(user);
            return user;
        }

        // send temporary password on email
        String temporaryPassword = passwordGenerator.generate();
        boolean sendResult = smtpService.sendEmail(dto.getEmail(), REGISTRATION_EMAIL_SUBJECT,
                genTemporaryPasswordEmailBody(dto.getLastName(), dto.getFirstName(),
                        dto.getMiddleName(), temporaryPassword));

        if(!sendResult){
            user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
            userRepo.save(user);
            return user;
        }

        // update DB (set temporaryPassword)
        String temporaryPasswordHashed = hasher.createHash(temporaryPassword);
        user.setStatus(UserEntity.Status.ACTIVATION_PENDING);
        user.setTemporaryPasswordHashed(temporaryPasswordHashed);
        user.setTemporaryPasswordExpiration(LocalDateTime.now().plusDays(7));
        userRepo.save(user);
        return user;
    }

    @Override
    @Transactional
    public UserEntity updateUser(Long id, AdminUpdateRequestDTO dto) {
        UserEntity user = getUserById(id);

        if(dto.getLastName() != null){
            user.setLastName(dto.getLastName());
        }
        if(dto.getFirstName() != null){
            user.setFirstName(dto.getFirstName());
        }
        if(dto.getMiddleName() != null){
            user.setMiddleName(dto.getMiddleName());
        }

        boolean emailUpdated = false;
        if(dto.getEmail() != null){
            if(!dto.getEmail().equals(user.getEmail())){
                user.setEmail(dto.getEmail());
                emailUpdated = true;
            }
        }
        userRepo.save(user);

        if(!emailUpdated){
            return user;
        }

        if(user.getStatus() == UserEntity.Status.ACTIVATED){
            /////////////////////////////////////////////////////// NEED TO Blacklist all tokens in Redis
            return user;
        }

        // there, account is not ACTIVATED, so need to resend email with temporary password

        if(user.getStatus() == UserEntity.Status.RECOVERY_PENDING){
            /////////////////////////////////////////////////////// NEED TO Blacklist all tokens in Redis
        }

        user.setStatus(null);
        user.setPasswordHashed(null);
        user.setTemporaryPasswordHashed(null);
        user.setTemporaryPasswordExpiration(null);
        userRepo.save(user);


        // check SMTP connection
        if(!smtpService.checkConnection()){
            user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
            userRepo.save(user);
            return user;
        }

        // send temporary password on email
        String temporaryPassword = passwordGenerator.generate();
        boolean sendResult = smtpService.sendEmail(user.getEmail(), TEMPORARY_PASSWORD_EMAIL_SUBJECT,
                genTemporaryPasswordEmailBody(user.getLastName(), user.getFirstName(),
                        user.getMiddleName(), temporaryPassword));

        if(!sendResult){
            user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
            userRepo.save(user);
            return user;
        }

        // update DB (set temporaryPassword)
        String temporaryPasswordHashed = hasher.createHash(temporaryPassword);
        user.setStatus(UserEntity.Status.ACTIVATION_PENDING);
        user.setTemporaryPasswordHashed(temporaryPasswordHashed);
        user.setTemporaryPasswordExpiration(LocalDateTime.now().plusDays(7));
        userRepo.save(user);
        return user;
    }

    @Override
    public UserEntity resendTemporaryPassword(Long id) throws ServiceUnavailableException {
        UserEntity user = getUserById(id);

        boolean isUserAlreadyActivated =
                user.getStatus() == UserEntity.Status.ACTIVATED ||
                user.getStatus() == UserEntity.Status.RECOVERY_PENDING;

        if(!isUserAlreadyActivated){
            user.setStatus(null);
        }
        user.setTemporaryPasswordHashed(null);
        user.setTemporaryPasswordExpiration(null);
        userRepo.save(user);

        // check SMTP connection
        if(!smtpService.checkConnection()){
            if(!isUserAlreadyActivated){
                user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
                userRepo.save(user);
            }
            throw new ServiceUnavailableException("SMTP is unavailable");
        }

        // send temporary password on email
        String temporaryPassword = passwordGenerator.generate();
        boolean sendResult = smtpService.sendEmail(user.getEmail(), TEMPORARY_PASSWORD_EMAIL_SUBJECT,
                genTemporaryPasswordEmailBody(user.getLastName(), user.getFirstName(),
                        user.getMiddleName(), temporaryPassword));

        if(!sendResult){
            if(!isUserAlreadyActivated){
                user.setStatus(UserEntity.Status.EMAIL_SENDING_FAILURE);
                userRepo.save(user);
            }
            throw new ServiceUnavailableException("Sending email is failed");
        }

        // update DB (set temporaryPassword)
        String temporaryPasswordHashed = hasher.createHash(temporaryPassword);
        if(!isUserAlreadyActivated){
            user.setStatus(UserEntity.Status.ACTIVATION_PENDING);
        }
        else{
            user.setStatus(UserEntity.Status.RECOVERY_PENDING);
        }
        user.setTemporaryPasswordHashed(temporaryPasswordHashed);
        user.setTemporaryPasswordExpiration(LocalDateTime.now().plusDays(7));
        userRepo.save(user);
        return user;
    }

    private String genTemporaryPasswordEmailBody(String lastName, String firstName, String middleName,
                                                 String temporaryPassword){
        return String.format(TEMPORARY_PASSWORD_EMAIL_BODY_TEMPLATE,
                lastName,
                firstName,
                middleName != null ? middleName : "",
                temporaryPassword,
                TEMPORARY_PASSWORD_EXPIRATION_DAYS
        );
    }


    @Override
    public UserEntity getUserById(Long id) {
        UserEntity user = userRepo.findById(id).orElse(null);
        if(user == null){
            throw new EntityNotFoundException(String.format("User with id=%d does not exist", id));
        }

        return user;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity user = getUserById(id);
        userRepo.delete(user);
    }
}
