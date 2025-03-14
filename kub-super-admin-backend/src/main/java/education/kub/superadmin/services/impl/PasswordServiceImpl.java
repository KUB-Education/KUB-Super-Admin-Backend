package education.kub.superadmin.services.impl;

import education.kub.superadmin.generators.password.inter.PasswordGenerator;
import education.kub.superadmin.helpers.hasher.inter.Hasher;
import education.kub.superadmin.services.inter.PasswordService;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    protected PasswordGenerator generator;
    protected Hasher hasher;

    public PasswordServiceImpl(PasswordGenerator generator, Hasher hasher) {
        this.generator = generator;
        this.hasher = hasher;
    }

    @Override
    public String hash(String password) {
        return this.hasher.createHash(password);
    }

    @Override
    public boolean check(String rawPassword, String hashedPassword) {
        return this.hasher.checkHash(rawPassword, hashedPassword);
    }

    @Override
    public String generate() {
        return this.generator.generate();
    }
}
