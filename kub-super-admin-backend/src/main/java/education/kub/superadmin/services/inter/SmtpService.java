package education.kub.superadmin.services.inter;

import org.springframework.stereotype.Service;

public interface SmtpService {
    public boolean sendEmail(String to, String subject, String body);

    public boolean checkConnection();
}
