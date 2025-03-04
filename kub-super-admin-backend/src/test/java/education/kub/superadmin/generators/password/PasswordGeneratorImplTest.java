package education.kub.superadmin.generators.password;

import education.kub.superadmin.generators.password.impl.PasswordGeneratorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordGeneratorImplTest {
    private PasswordGeneratorImpl passwordGenerator;

    @BeforeEach
    public void setUp() {
        this.passwordGenerator = new PasswordGeneratorImpl();
    }

    @Test
    public void testGenerateWithDefaultRules() {
        String password = this.passwordGenerator.generate();
        String defaultSpecialChars = this.passwordGenerator.getSpecialCharsDefault();
        int lengthDefault = this.passwordGenerator.getLengthDefault();

        System.out.println("Generated password is: " + password);

        assertNotNull(password, "Generated password should not be null");

        // Assert password is at least 8 characters long
        assertTrue(
                password.length() >= lengthDefault,
                "Password must be at least " + lengthDefault + " characters long"
        );

        assertTrue(
                Pattern.compile("[A-Z]").matcher(password).find(),
                "Password must contain at least one uppercase letter"
        );

        assertTrue(
                Pattern.compile("[a-z]").matcher(password).find(),
                "Password must contain at least one lowercase letter"
        );

        assertTrue(
                Pattern.compile("[0-9]").matcher(password).find(),
                "Password must contain at least one digit"
        );

        assertTrue(Pattern.compile(
                        "[" + Pattern.quote(defaultSpecialChars) + "]").matcher(password).find(),
                "Password must contain at least one special character from the set: " + defaultSpecialChars
        );
    }
}
