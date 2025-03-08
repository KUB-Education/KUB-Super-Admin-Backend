package education.kub.superadmin.helpers.hasher.impl.extend;

import education.kub.superadmin.helpers.hasher.impl.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordHasher extends PasswordHasher {
    public BCryptPasswordHasher() {
        super(new BCryptPasswordEncoder());
    }
}
