package education.kub.superadmin.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class SmtpService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public SmtpService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            return true;
        } catch (Exception e) {
            System.out.println("SMTP sending failed: " + e.getMessage());
            return false;
        }
    }

    public boolean checkConnection() {
        try {
            ((JavaMailSenderImpl) mailSender).testConnection();

            return true;
        } catch (Exception e) {
            System.err.println("SMTP connection failed: " + e.getMessage());

            return false;
        }
    }
}
