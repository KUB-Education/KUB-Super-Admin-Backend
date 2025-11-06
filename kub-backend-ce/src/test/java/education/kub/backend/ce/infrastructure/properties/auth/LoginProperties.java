package education.kub.backend.ce.infrastructure.properties.auth;

import education.kub.backend.ce.infrastructure.properties.user.UserProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginProperties {
    public UserProperties user = UserProperties.builder().build();

    public String accessToken;
    public String refreshToken;
}
