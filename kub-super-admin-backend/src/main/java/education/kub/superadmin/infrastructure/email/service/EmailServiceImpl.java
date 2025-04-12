package education.kub.superadmin.infrastructure.email.service;

import education.kub.superadmin.app.properties.AppAccountRegistrationProperties;
import education.kub.superadmin.infrastructure.email.factory.EmailMessageFactory;
import education.kub.superadmin.infrastructure.email.model.EmailMessage;
import education.kub.superadmin.domain.user.model.UserRequestDTO;
import education.kub.superadmin.infrastructure.email.smtp.SmtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final SmtpService smtpService;

    private final AppAccountRegistrationProperties appAccountRegistrationProperties;

    @Override
    public boolean sendRegistrationEmail(UserRequestDTO userRequestDTO, String temporaryPassword) {
        EmailMessage emailMessage = EmailMessageFactory.registrationEmailMessage(
                userRequestDTO,
                temporaryPassword,
                appAccountRegistrationProperties.temporaryPasswordExpirationDays()
        );

        return smtpService.sendEmail(emailMessage);
    }
}
