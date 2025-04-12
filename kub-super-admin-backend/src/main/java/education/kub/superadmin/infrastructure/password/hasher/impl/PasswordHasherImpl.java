package education.kub.superadmin.infrastructure.password.hasher.impl;

import education.kub.superadmin.infrastructure.password.hasher.inter.PasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHasherImpl implements PasswordHasher
{
    private final PasswordEncoder encoder;

    public PasswordHasherImpl(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String hash(String rawPassword) {
        return this.encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return this.encoder.matches(rawPassword, hashedPassword);
    }
}
