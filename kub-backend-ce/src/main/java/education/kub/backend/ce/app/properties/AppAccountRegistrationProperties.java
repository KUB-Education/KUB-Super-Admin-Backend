package education.kub.backend.ce.app.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.account-registration")
public record AppAccountRegistrationProperties(
        int temporaryPasswordExpirationDays
) {}
