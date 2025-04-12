package education.kub.superadmin.infrastructure.email.smtp;

import education.kub.superadmin.infrastructure.email.model.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmtpServiceImpl implements SmtpService {
    private final JavaMailSender mailSender;

    private final MailProperties mailProperties;

    @Override
    public boolean sendEmail(EmailMessage emailMessage) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailProperties.getUsername());
            message.setTo(emailMessage.to());
            message.setSubject(emailMessage.subject());
            message.setText(emailMessage.body());

            mailSender.send(message);

            return true;
        } catch (Exception e) {
            System.out.println("SMTP sending failed: " + e.getMessage());
            return false;
        }
    }
}
