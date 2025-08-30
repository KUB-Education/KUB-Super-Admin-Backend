package education.kub.backend.ce.infrastructure.email.service;

import education.kub.backend.ce.domain.user.model.UserRegistrationData;

public interface EmailService {
    boolean sendRegistrationEmail(UserRegistrationData userRegistrationData, String temporaryPassword);
}
