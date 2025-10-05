package education.kub.backend.ce.domain.room.model;

public record RoomDto(
        Long id,

        String location,

        Short capacity,

        String details
) {
}
