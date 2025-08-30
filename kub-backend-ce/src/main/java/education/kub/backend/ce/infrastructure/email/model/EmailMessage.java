package education.kub.backend.ce.infrastructure.email.model;

public record EmailMessage(
        String to,

        String subject,

        String body
) {}
