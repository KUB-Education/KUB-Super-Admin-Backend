package education.kub.superadmin.infrastructure.password.service;

public interface PasswordService {
    String hash(String rawPassword);

    boolean matches(String rawPassword, String hashedPassword);

    String generate();
}
