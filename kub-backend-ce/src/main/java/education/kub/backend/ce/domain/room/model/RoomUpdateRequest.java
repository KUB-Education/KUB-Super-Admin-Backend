package education.kub.backend.ce.domain.room.model;

public record RoomUpdateRequest(
        String location,

        Short capacity,

        String description
) {
}