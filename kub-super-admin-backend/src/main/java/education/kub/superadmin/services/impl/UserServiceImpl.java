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
import education.kub.superadmin.services.inter.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final SmtpService smtpService;

    private final IPasswordGenerator passwordGenerator = new PasswordGeneratorImpl();
    private final IHasher hasher = new BCryptPasswordHasher();

    private final String REGISTRATION_EMAIL_SUBJECT = "Registration on KUB Education";
    private final String REGISTRATION_EMAIL_BODY_TEMPLATE =
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
                genRegistrationEmailBody(dto, temporaryPassword));

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

    private String genRegistrationEmailBody(AdminRequestDTO dto, String temporaryPassword){
        return String.format(REGISTRATION_EMAIL_BODY_TEMPLATE,
                dto.getLastName(),
                dto.getFirstName(),
                dto.getMiddleName() != null ? dto.getMiddleName() : "",
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
