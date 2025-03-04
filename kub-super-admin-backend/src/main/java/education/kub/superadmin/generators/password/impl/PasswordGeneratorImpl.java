package education.kub.superadmin.generators.password.impl;

import education.kub.superadmin.generators.password.IPasswordGenerator;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;

import java.util.*;

public class PasswordGeneratorImpl implements IPasswordGenerator {
    private static final int LENGTH_DEFAULT = 8;

    // Rule names
    private static final String LOWERCASE_LETTER_RULENAME = "lowerCaseLetter";
    private static final String UPPERCASE_LETTER_RULENAME = "upperCaseLetter";
    private static final String DIGITS_RULENAME = "digits";
    private static final String SPECIAL_CHARS_RULENAME = "specialChars";


    private static final String MIN_COUNT_KEY = "minCount";
    private static final String SOURCE_KEY = "source"; // key by which string of special characters can be passed
    private static final int MIN_COUNT_DEFAULT = 1;
    private static final String SPECIAL_CHARS_DEFAULT = "~`!@#$%^&*()_-+={}[]|\\:;\"'<>.?/";


    private static final String ERROR_CODE = "ERRONEOUS_SPECIAL_CHARS";

    /**
     * Default password rules:
     * 1) Must be at least 8 characters long.
     * 2) Must contain at least one uppercase Latin letter (A-Z).
     * 3) Must contain at least one lowercase Latin letter (a-z).
     * 4) Must contain at least one digit (0-9).
     * 5) Must contain at least one special character from: ~`!@#$%^&*()_-+={}[]|\:;"'<>.?/
     */
    private Map<String, Map<String, Object>> getDefaultRules() {
        return Map.of(
                LOWERCASE_LETTER_RULENAME, Map.of(
                        MIN_COUNT_KEY, MIN_COUNT_DEFAULT
                ),
                UPPERCASE_LETTER_RULENAME, Map.of(
                        MIN_COUNT_KEY, MIN_COUNT_DEFAULT
                ),
                DIGITS_RULENAME, Map.of(
                        MIN_COUNT_KEY, MIN_COUNT_DEFAULT
                ),
                SPECIAL_CHARS_RULENAME, Map.of(
                        MIN_COUNT_KEY, MIN_COUNT_DEFAULT,
                        SOURCE_KEY, SPECIAL_CHARS_DEFAULT
                )
        );
    }

    public String getSpecialCharsDefault() {
        return SPECIAL_CHARS_DEFAULT;
    }

    public int getLengthDefault() {
        return LENGTH_DEFAULT;
    }

    @Override
    public String generate(Map<String, Map<String, Object>> rulesConfig) {
        return this.generate(LENGTH_DEFAULT, rulesConfig);
    }

    @Override
    public String generate(int length) {
        return this.generate(length, getDefaultRules());
    }

    @Override
    public String generate() {
        return this.generate(LENGTH_DEFAULT, getDefaultRules());
    }

    @Override
    public String generate(int length, Map<String, Map<String, Object>> rulesConfig) {
        org.passay.PasswordGenerator generator = new org.passay.PasswordGenerator();

        List<CharacterRule> characterRules = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> entry : rulesConfig.entrySet()) {
            String ruleName = entry.getKey();
            Map<String, Object> params = entry.getValue();

            int minCount = (int) params.getOrDefault(MIN_COUNT_KEY, MIN_COUNT_DEFAULT);
            String source = (String) params.getOrDefault(SOURCE_KEY, SPECIAL_CHARS_DEFAULT);

            CharacterData characterSource;

            switch (ruleName) {
                case LOWERCASE_LETTER_RULENAME:
                    characterSource = EnglishCharacterData.LowerCase;
                    break;
                case UPPERCASE_LETTER_RULENAME:
                    characterSource = EnglishCharacterData.UpperCase;
                    break;
                case DIGITS_RULENAME:
                    characterSource = EnglishCharacterData.Digit;
                    break;
                case SPECIAL_CHARS_RULENAME:
                    characterSource = new CharacterData() {
                        @Override
                        public String getErrorCode() {
                            return ERROR_CODE;
                        }

                        @Override
                        public String getCharacters() {
                            return source;
                        }
                    };
                    break;
                default:
                    throw new IllegalArgumentException("Unknown rule: " + ruleName);
            }
            CharacterRule rule = new CharacterRule(characterSource);
            rule.setNumberOfCharacters(minCount);
            characterRules.add(rule);
        }

        return generator.generatePassword(length, characterRules);
    }
}
