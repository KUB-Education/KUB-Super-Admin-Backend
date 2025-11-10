package education.kub.backend.ce.infrastructure.properties.executor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.test.web.servlet.MockMvc;

@Getter
@Setter
public class ExecutorProperties {
    public ConnectionProperties conn;
    public MockMvc mvc;
}
