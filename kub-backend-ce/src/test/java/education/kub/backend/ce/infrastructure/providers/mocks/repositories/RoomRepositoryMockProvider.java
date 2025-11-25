package education.kub.backend.ce.infrastructure.providers.mocks.repositories;

import education.kub.backend.ce.domain.room.entity.RoomEntity;
import education.kub.backend.ce.domain.room.repository.RoomRepository;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

public class RoomRepositoryMockProvider {
    private static long id_count = 0;
    private static Set<RoomEntity> rooms = new HashSet<>();

    private static void saveRoom(RoomRepository mock, RoomEntity room) {
        Mockito.lenient().doReturn(Optional.of(room)).when(mock).findById(room.getId());
        Mockito.lenient().doReturn(true).when(mock).existsByLocation(room.getLocation());
        Mockito.lenient().doReturn(true).when(mock).existsByLocationAndIdNot(room.getLocation(), room.getId());
        rooms.add(room);
    }

    public static void resetMocks() {
        id_count = 0;
        rooms.clear();
    }

    public static RoomRepository createRoomRepositoryMock() {
        RoomRepository roomRepo = Mockito.mock(RoomRepository.class);

        Mockito.lenient().doAnswer(invocation -> {
            var saved_room = invocation.getArgument(0, RoomEntity.class);
            var room_id = saved_room.getId();
            if (room_id == null || !(room_id >= 0 && room_id < id_count)) {
                saved_room.setId(id_count++);
            }
            saveRoom(roomRepo, saved_room);
            return saved_room;
        }).when(roomRepo).save(any(RoomEntity.class));
        Mockito.lenient().doAnswer(invocation -> {
            var location = invocation.getArgument(0, String.class);
            return rooms.stream().filter(roomEntity -> roomEntity.getLocation().equals(location)).toList();
        }).when(roomRepo).findByLocationContaining(any(String.class));
        Mockito.lenient().doAnswer(invocation -> {
            var capacity = invocation.getArgument(0, Short.class);
            return rooms.stream().filter(roomEntity -> roomEntity.getCapacity().equals(capacity)).toList();
        }).when(roomRepo).findByCapacityGreaterThanEqual(any(Short.class));
        Mockito.lenient().doAnswer(invocation -> {
            var location = invocation.getArgument(0, String.class);
            var capacity = invocation.getArgument(1, Short.class);
            return rooms.stream().filter(roomEntity -> roomEntity.getLocation().equals(location)).
                    filter(roomEntity -> roomEntity.getCapacity() >= capacity).toList();
        }).when(roomRepo).findByLocationContainingAndCapacityGreaterThanEqual(any(String.class),any(Short.class));
        Mockito.lenient().doReturn(rooms.stream().toList()).when(roomRepo).findAll();

        return roomRepo;
    }
}
