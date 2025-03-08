package education.kub.superadmin.helpers.hasher.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordHasherImpl extends PasswordHasherImpl {
    public BCryptPasswordHasherImpl() {
        super(new BCryptPasswordEncoder());
    }
}
