package education.kub.backend.ce.domain.room.service;

import education.kub.backend.ce.domain.room.entity.RoomEntity;
import education.kub.backend.ce.domain.room.model.RoomCreateRequest;
import education.kub.backend.ce.domain.room.model.RoomDto;
import education.kub.backend.ce.domain.room.model.RoomRequestFilter;
import education.kub.backend.ce.domain.room.model.RoomUpdateRequest;
import education.kub.backend.ce.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomServiceTest {
    private final RoomRepository roomRepo;
    private final RoomService roomService;

    @Autowired
    public RoomServiceTest(RoomRepository roomRepo, RoomService roomService){
        this.roomRepo = roomRepo;
        this.roomService = roomService;
    }


    @BeforeEach
    void setUp() {
        roomRepo.deleteAll();
    }


    @Test
    void testCreateRoom() {
        RoomCreateRequest createRequest = new RoomCreateRequest("location", (short)100, "details");

        RoomDto createdRoom = roomService.createRoom(createRequest);
        assertEquals(createRequest.location(), createdRoom.location());
        assertEquals(createRequest.capacity(), createdRoom.capacity());
        assertEquals(createRequest.description(), createdRoom.description());

        RoomEntity createdRoomFromRepo = roomRepo.findById(createdRoom.id()).get();
        assertEquals(createdRoomFromRepo.getId(), createdRoom.id());
        assertEquals(createdRoomFromRepo.getLocation(), createdRoom.location());
        assertEquals(createdRoomFromRepo.getCapacity(), createdRoom.capacity());
        assertEquals(createdRoomFromRepo.getDescription(), createdRoom.description());
    }

    @Test
    void getRoomById() {
        RoomEntity roomExpected = new RoomEntity(null, "location", (short)100, "details");
        roomRepo.save(roomExpected);

        RoomDto roomActual = roomService.getRoomById(roomExpected.getId());

        assertEquals(roomExpected.getId(), roomActual.id());
        assertEquals(roomExpected.getLocation(), roomActual.location());
        assertEquals(roomExpected.getCapacity(), roomActual.capacity());
        assertEquals(roomExpected.getDescription(), roomActual.description());
    }

    @Test
    void getRoomsByFilter() {
        RoomEntity room1 = new RoomEntity(null, "location11", (short)100, "details");
        RoomEntity room2 = new RoomEntity(null, "location23", (short)150, "details");
        RoomEntity room3 = new RoomEntity(null, "location31", (short)160, "details");
        roomRepo.save(room1);
        roomRepo.save(room2);
        roomRepo.save(room3);

        List<RoomDto> rooms = roomService.getRoomsByFilter(new RoomRequestFilter("1", (short)101));
        // rooms should contain only room3

        assertEquals(1, rooms.size());
        assertEquals(room3.getId(), rooms.get(0).id());
        assertEquals(room3.getLocation(), rooms.get(0).location());
        assertEquals(room3.getCapacity(), rooms.get(0).capacity());
        assertEquals(room3.getDescription(), rooms.get(0).description());
    }

    @Test
    void updateRoom() {
        RoomEntity roomExpected = new RoomEntity(null, "location", (short)100, "details");
        roomRepo.save(roomExpected);

        roomService.updateRoom(roomExpected.getId(),
                new RoomUpdateRequest("new_location", (short)150, null));

        roomExpected.setLocation("new_location");
        roomExpected.setCapacity((short)150);

        RoomEntity roomActual = roomRepo.findById(roomExpected.getId()).get();


        assertEquals(roomExpected.getId(), roomActual.getId());
        assertEquals(roomExpected.getLocation(), roomActual.getLocation());
        assertEquals(roomExpected.getCapacity(), roomActual.getCapacity());
        assertEquals(roomExpected.getDescription(), roomActual.getDescription());
    }

    @Test
    void deleteRoom() {
        RoomEntity room = new RoomEntity(null, "location", (short)100, "details");
        roomRepo.save(room);

        roomService.deleteRoom(room.getId());

        assertFalse(roomRepo.existsById(room.getId()));
    }
}