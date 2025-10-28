package education.kub.backend.ce.infrastructure.properties.auth;

import education.kub.backend.ce.infrastructure.properties.executor.ExecutorProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class LoginProperties {
    public ExecutorProperties executor = new ExecutorProperties();

    public final String email = "allex.nevedrov@example.com";
    public final String password = "password";

    public String accessToken;
    public String refreshToken;
}
