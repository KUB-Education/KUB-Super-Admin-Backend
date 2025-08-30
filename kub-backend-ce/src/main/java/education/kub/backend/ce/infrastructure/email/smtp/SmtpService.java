package education.kub.backend.ce.infrastructure.email.smtp;

import education.kub.backend.ce.infrastructure.email.model.EmailMessage;

public interface SmtpService {
    boolean sendEmail(EmailMessage emailMessage);
}
