package education.kub.superadmin.generators.password;

import java.util.Map;

public interface IPasswordGenerator {
    /**
     * Generates a password based on the specified rules and length.
     *
     * @param length the length of the password to be generated.
     * @param rulesConfig a map of rules that the password must satisfy. Each entry in the map should contain
     *                    the rule name as the key (e.g., "lowerCaseLetter", "specialChars") and the rule configuration
     *                    (such as minimum count, source characters, etc.).
     *                    If the source for "specialChars" is not specified, a default set of special characters will be applied.
     * @return the generated password that meets the specified rules.
     *
     * Example of rulesConfig map:
     * Map<String, Map<String, Object>> rulesConfig = Map.of(
     *     "lowerCaseLetter", Map.of("minCount", 1),
     *     "upperCaseLetter", Map.of("minCount", 1),
     *     "digits", Map.of("minCount", 1),
     *     "specialChars", Map.of("minCount", 1, "source", "!@#$%^&*")
     * );
     */
    public String generate(int length, Map<String, Map<String, Object>> rulesConfig);

    /**
     * Generates a password based on the specified rules. The default length of 8 characters will be used.
     *
     * @param rulesConfig a map of rules that the password must satisfy. Each entry in the map should contain
     *                    the rule name as the key (e.g., "lowerCaseLetter", "specialChars") and the rule configuration
     *                    (such as minimum count, source characters, etc.).
     *                    If the source for "specialChars" is not specified, a default set of special characters will be applied.
     * @return the generated password that meets the specified rules.
     *
     * Example of rulesConfig map:
     * Map<String, Map<String, Object>> rulesConfig = Map.of(
     *     "lowerCaseLetter", Map.of("minCount", 1),
     *     "upperCaseLetter", Map.of("minCount", 1),
     *     "digits", Map.of("minCount", 1),
     *     "specialChars", Map.of("minCount", 1, "source", "!@#$%^&*")
     * );
     */
    public String generate(Map<String, Map<String, Object>> rulesConfig);

    /**
     * Generates a password with the specified length and default rules. The default set of rules will be applied.
     *
     * @param length the length of the password to be generated. If the length is invalid or not specified,
     *               a default length of 8 will be used.
     * @return the generated password that meets the default rules.
     */
    public String generate(int length);

    /**
     * Generates a password using the default length (8 characters) and default rules.
     *
     * @return the generated password that meets the default rules.
     */
    public String generate();
}
