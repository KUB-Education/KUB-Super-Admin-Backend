package education.kub.backend.ce.infrastructure.email.service;

import education.kub.backend.ce.app.properties.AppAccountRegistrationProperties;
import education.kub.backend.ce.domain.user.model.UserRegistrationData;
import education.kub.backend.ce.infrastructure.email.factory.EmailMessageFactory;
import education.kub.backend.ce.infrastructure.email.model.EmailMessage;
import education.kub.backend.ce.infrastructure.email.smtp.SmtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final SmtpService smtpService;

    private final AppAccountRegistrationProperties appAccountRegistrationProperties;

    @Override
    public boolean sendRegistrationEmail(UserRegistrationData userRegistrationData, String temporaryPassword) {
        EmailMessage emailMessage = EmailMessageFactory.registrationEmailMessage(
                userRegistrationData,
                temporaryPassword,
                appAccountRegistrationProperties.temporaryPasswordExpirationDays()
        );

        return smtpService.sendEmail(emailMessage);
    }
}
