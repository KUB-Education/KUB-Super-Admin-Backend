package education.kub.backend.ce.infrastructure.properties.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountRequestProperties {
    public final String new_password = "password";
}
