package education.kub.backend.ce.app.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record AppSecurityProperties(
        String secretKey,

        long accessTokenValidityMs,

        long refreshTokenValidityMs
) {}
