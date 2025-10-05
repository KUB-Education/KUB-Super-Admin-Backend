package education.kub.backend.ce.domain.room.model;

public record RoomRequestFilter(
        String locationContains,

        Short minCapacity
) {
}