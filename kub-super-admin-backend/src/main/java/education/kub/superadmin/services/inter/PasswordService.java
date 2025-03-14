package education.kub.superadmin.services.inter;

/**
 * Interface for password hashing and generation functionality.
 */
public interface PasswordService {

    /**
     * Hashes the provided password using a bcrypt hashing algorithm.
     *
     * @param password the plain text password to hash.
     * @return the hashed password.
     */
    String hash(String password);

    /**
     * Verifies if the provided plain text password matches the stored hashed password.
     *
     * @param rawPassword the plain text password to verify.
     * @param hashedPassword the hashed password for comparison.
     * @return true if the passwords match, false otherwise.
     */
    boolean check(String rawPassword, String hashedPassword);

    /**
     * Generates a random password using the default length (8 characters)
     * and predefined security rules (e.g., letters, numbers, symbols).
     *
     * @return a randomly generated secure password.
     */
    String generate();
}
