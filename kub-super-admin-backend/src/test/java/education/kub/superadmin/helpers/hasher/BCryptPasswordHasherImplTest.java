package education.kub.superadmin.helpers.hasher;

import education.kub.superadmin.helpers.hasher.impl.BCryptPasswordHasherImpl;
import education.kub.superadmin.helpers.hasher.inter.Hasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BCryptPasswordHasherImplTest {
    private Hasher hasher;

    @BeforeEach
    public void setUp() {
        this.hasher = new BCryptPasswordHasherImpl();
    }

    @Test
    public void testCreateAndCheckBcryptHash()
    {
        String rawPassword = "H?!3pIx3";
        String hashedPassword = this.hasher.createHash(rawPassword);

        assertTrue(this.hasher.checkHash(rawPassword, hashedPassword));
    }
}
