package education.kub.backend.ce.unit;

import education.kub.backend.ce.infrastructure.password.generator.PasswordGeneratorImpl;
import education.kub.backend.ce.infrastructure.password.hasher.impl.BCryptPasswordHasherImpl;
import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.infrastructure.password.service.PasswordServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {
        PasswordServiceImpl.class,
        PasswordGeneratorImpl.class,
        BCryptPasswordHasherImpl.class,
})
public class PasswordServiceTest {
    @Autowired
    PasswordService passwordService;

    @Test
    void shouldGeneratePasswordThatMatchesItsHashedVersion() {
        String password = passwordService.generate();

        assertFalse(password.isBlank());

        String hashedPassword = passwordService.hash(password);

        assertFalse(hashedPassword.isBlank());

        assertTrue(passwordService.matches(password, hashedPassword));
    }
}
