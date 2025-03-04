package education.kub.superadmin.helpers.hasher.impl;

import education.kub.superadmin.helpers.hasher.IHasher;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHasher implements IHasher
{
    private final PasswordEncoder encoder;

    public PasswordHasher(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String createHash(String string) {
        return this.encoder.encode(string);
    }

    @Override
    public boolean checkHash(String rawString, String hashedString) {
        return this.encoder.matches(rawString, hashedString);
    }
}
