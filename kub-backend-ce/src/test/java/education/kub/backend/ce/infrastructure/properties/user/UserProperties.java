package education.kub.backend.ce.infrastructure.properties.user;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Builder
public class UserProperties {
    @Builder.Default
    public long id = 0;

    @Builder.Default
    public Set<RoleEntity.Type> roles = new HashSet<>(Arrays.asList(RoleEntity.Type.USER));

    @Builder.Default
    public String last_name = "Doe";
    @Builder.Default
    public String first_name = "John";
    @Builder.Default
    public String middle_name = "Edward";
    @Builder.Default
    public String email = "doe.john@example.com";
    @Builder.Default
    public String password = "password";

    public UserRequestUrlParameters toUrlProperties() {
        return UserRequestUrlParameters.builder().user_id(Long.toString(id)).build();
    }

    public Map<String,String> toMap() {
        var map = new HashMap<String, String>();
        if (first_name != null) {
            map.put("first_name", first_name);
        }
        if (middle_name != null) {
            map.put("middle_name", middle_name);
        }
        if (last_name != null) {
            map.put("last_name", last_name);
        }
        if (email != null) {
            map.put("email", email);
        }
        return map;
    }
}
