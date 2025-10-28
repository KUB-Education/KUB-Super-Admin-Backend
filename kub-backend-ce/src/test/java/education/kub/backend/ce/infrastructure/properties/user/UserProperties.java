package education.kub.backend.ce.infrastructure.properties.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserProperties {
    public final long id = 0;

    @Autowired
    public RoleProperties roles;

    public final String last_name = "Doe";
    public final String first_name = "John";
    public final String middle_name = "Edward";
}
