package education.kub.superadmin.infrastructure.email.factory;

import education.kub.superadmin.domain.user.model.UserRequestDTO;
import education.kub.superadmin.infrastructure.email.model.EmailMessage;

public class EmailMessageFactory {
    public static EmailMessage registrationEmailMessage(
            UserRequestDTO userRequestDTO,

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
                userRequestDTO.lastName(),
                userRequestDTO.firstName(),
                userRequestDTO.middleName(),
                temporaryPassword,
                temporaryPasswordExpirationDays
        );

        return new EmailMessage(userRequestDTO.email(), subject, body);
    };
}
