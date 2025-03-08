package education.kub.superadmin.helpers.hasher;

import education.kub.superadmin.helpers.hasher.impl.extend.BCryptPasswordHasher;
import education.kub.superadmin.helpers.hasher.inter.Hasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BCryptPasswordHasherTest {
    private Hasher hasher;

    @BeforeEach
    public void setUp() {
        this.hasher = new BCryptPasswordHasher();
    }

    @Test
    public void testCreateAndCheckBcryptHash()
    {
        String rawPassword = "H?!3pIx3";
        String hashedPassword = this.hasher.createHash(rawPassword);

        assertTrue(this.hasher.checkHash(rawPassword, hashedPassword));
    }
}
