package education.kub.backend.ce.infrastructure.properties.room;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomRequestUrlParameters {
    @Builder.Default
    public String room_id = "0";

    public String toUrlRequestParametersSubstring() {
        return room_id;
    }
}

