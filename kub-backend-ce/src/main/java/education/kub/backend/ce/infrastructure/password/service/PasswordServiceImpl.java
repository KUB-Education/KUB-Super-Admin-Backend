package education.kub.backend.ce.infrastructure.password.service;

import education.kub.backend.ce.infrastructure.password.generator.PasswordGenerator;
import education.kub.backend.ce.infrastructure.password.hasher.inter.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final PasswordGenerator passwordGenerator;
    private final PasswordHasher passwordHasher;

    @Override
    public String hash(String rawPassword) {
        return this.passwordHasher.hash(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return this.passwordHasher.matches(rawPassword, hashedPassword);
    }

    @Override
    public String generate() {
        return this.passwordGenerator.generate();
    }
}
