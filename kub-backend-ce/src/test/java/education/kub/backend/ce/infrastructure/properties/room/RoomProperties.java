package education.kub.backend.ce.infrastructure.properties.room;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
public class RoomProperties {
    @Builder.Default
    private Long id = 0L;
    @Builder.Default
    private String location = "Main Campus - B201";
    @Builder.Default
    private Short capacity = 1;
    @Builder.Default
    private String description = "Test";

    public RoomRequestUrlParameters toUrlParameters() {
        return RoomRequestUrlParameters.builder().room_id(id.toString()).build();
    }

    public Map<String, Object> toMap() {
        var map = new HashMap<String, Object>();
        if (location != null) {
            map.put("location", location);
        }
        if (capacity != null) {
            map.put("capacity", capacity.toString());
        }
        if (description != null) {
            map.put("description", description);
        }
        return map;
    }
}
