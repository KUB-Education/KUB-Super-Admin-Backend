package education.kub.backend.ce.infrastructure.components.room;

import education.kub.backend.ce.domain.room.entity.RoomEntity;
import education.kub.backend.ce.domain.room.mapper.RoomMapper;
import education.kub.backend.ce.domain.room.repository.RoomRepository;
import education.kub.backend.ce.domain.room.service.RoomService;
import education.kub.backend.ce.infrastructure.properties.room.RoomProperties;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.RoomRepositoryMockProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomComponent {
    @Autowired
    public RoomRepository roomRepo;
    @Autowired
    public RoomMapper roomMapper;

    public void initRepo() {
        roomRepo.deleteAll();
    }

    public void mockRepo() {
        roomRepo = RoomRepositoryMockProvider.createRoomRepositoryMock();
    }

    public RoomEntity saveRoom(RoomProperties roomData) {
        RoomEntity room = createRoom(roomData);
        roomRepo.save(room);
        return room;
    }

    private RoomEntity createRoom(RoomProperties roomData) {
        var roomEntity = new RoomEntity();
        roomEntity.setId(roomData.getId());
        roomEntity.setLocation(roomData.getLocation());
        roomEntity.setCapacity(roomData.getCapacity());
        roomEntity.setDescription(roomData.getDescription());
        return roomEntity;
    }

    public RoomService BuildRoomService() {
        return new RoomService(roomRepo, roomMapper);
    }
}
