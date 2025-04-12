package education.kub.superadmin.infrastructure.email.smtp;

import education.kub.superadmin.infrastructure.email.model.EmailMessage;

public interface SmtpService {
    boolean sendEmail(EmailMessage emailMessage);
}
