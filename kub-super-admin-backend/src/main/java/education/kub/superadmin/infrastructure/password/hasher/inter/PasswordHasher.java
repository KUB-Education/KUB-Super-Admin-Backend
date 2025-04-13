package education.kub.superadmin.infrastructure.password.hasher.inter;

public interface PasswordHasher {
    String hash(String rawPassword);

    boolean matches(String rawPassword, String hashedPassword);
}
