package education.kub.backend.ce.infrastructure.password.hasher.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordHasherImpl extends PasswordHasherImpl {
    public BCryptPasswordHasherImpl() {
        super(new BCryptPasswordEncoder());
    }
}
