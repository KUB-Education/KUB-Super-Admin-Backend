package education.kub.superadmin.app.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record AppSecurityProperties(
        String superAdminHeaderSecret
) {}
