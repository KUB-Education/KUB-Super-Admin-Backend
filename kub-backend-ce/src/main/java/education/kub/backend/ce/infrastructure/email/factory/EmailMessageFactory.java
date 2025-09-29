package education.kub.backend.ce.infrastructure.email.factory;

import education.kub.backend.ce.domain.user.model.UserRegistrationData;
import education.kub.backend.ce.infrastructure.email.model.EmailMessage;

public class EmailMessageFactory {
    public static EmailMessage registrationEmailMessage(
            UserRegistrationData userRegistrationData,

            String temporaryPassword,

            int temporaryPasswordExpirationDays
    ) {
        String subject = "Welcome to KUB Education";

        String body = String.format("""
                Dear %s %s %s,
                   \s
                    Welcome to KUB Education!
                   \s
                    Your temporary password is: %s
                    It will expire in %d days.
                
                    Please log in and change it immediately.
                   \s
                    Regards,
                    KUB Education Team
                """,
                userRegistrationData.lastName(),
                userRegistrationData.firstName(),
                userRegistrationData.middleName(),
                temporaryPassword,
                temporaryPasswordExpirationDays
        );

        return new EmailMessage(userRegistrationData.email(), subject, body);
    };
}
