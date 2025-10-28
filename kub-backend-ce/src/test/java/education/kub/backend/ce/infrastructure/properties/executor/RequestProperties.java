package education.kub.backend.ce.infrastructure.properties.executor;

import io.restassured.http.Method;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;

import java.util.Map;

@Getter
@Setter
@Builder
public class RequestProperties {
    public String url;
    @Builder.Default
    public Method method = Method.POST;
    public Map<String,String> body;
    public Map<String,String> headers;
    @Builder.Default
    public String contentType = "application/json";
    @Builder.Default
    public MediaType acceptMediaType = MediaType.APPLICATION_JSON;
}
