package education.kub.superadmin.infrastructure.email.model;

public record EmailMessage(
        String to,

        String subject,

        String body
) {}
