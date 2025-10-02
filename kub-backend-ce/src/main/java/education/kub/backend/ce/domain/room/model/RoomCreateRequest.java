package education.kub.backend.ce.domain.room.model;

public record RoomCreateRequest(
        String location,

        Short capacity,

        String details
) {
}
