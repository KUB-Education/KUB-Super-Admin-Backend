package education.kub.superadmin.services.inter;

import org.springframework.stereotype.Service;

public interface SmtpService {
    boolean sendEmail(String to, String subject, String body);

    boolean checkConnection();
}
