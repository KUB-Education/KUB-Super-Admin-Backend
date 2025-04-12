package education.kub.superadmin.infrastructure.email.service;

import education.kub.superadmin.domain.user.model.UserRequestDTO;

public interface EmailService {
    boolean sendRegistrationEmail(UserRequestDTO userRequestDTO, String temporaryPassword);
}
