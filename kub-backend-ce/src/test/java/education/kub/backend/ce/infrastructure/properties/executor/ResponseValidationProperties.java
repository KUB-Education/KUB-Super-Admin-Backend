package education.kub.backend.ce.infrastructure.properties.executor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@Builder
public class ResponseValidationProperties {
    @Builder.Default
    public HttpStatusCode StatusCode = HttpStatus.OK;
    public String ValidationSchema;
}
