package education.kub.backend.ce.infrastructure.properties.executor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ConnectionProperties {
    @Value("${server.url}")
    public String base_url;
}
