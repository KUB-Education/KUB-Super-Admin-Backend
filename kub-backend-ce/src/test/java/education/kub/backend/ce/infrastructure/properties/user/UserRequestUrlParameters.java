package education.kub.backend.ce.infrastructure.properties.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequestUrlParameters {
    @Builder.Default
    public String user_id = "0";
    @Builder.Default
    public String role_id = "0";
}
