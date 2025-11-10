package education.kub.backend.ce.domain.room.repository;

import education.kub.backend.ce.domain.room.entity.RoomEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomRepositoryTest {
    @Autowired
    private RoomRepository roomRepo;

    @AfterEach
    void tearDown() {
//        roomRepo.deleteAll();
    }


    @Test
    void givenNew_whenSave_thenSuccess() {
        RoomEntity roomExpected = new RoomEntity();
        roomExpected.setLocation("01");
        roomExpected.setCapacity((short)100);
        roomExpected.setDetails("lecture hall");

        assertDoesNotThrow(() -> roomRepo.save(roomExpected));

        RoomEntity roomActual = roomRepo.findById(roomExpected.getId()).get();

        assertEquals(roomExpected, roomActual);
        assertEquals(roomExpected.getLocation(), roomActual.getLocation());
        assertEquals(roomExpected.getCapacity(), roomActual.getCapacity());
        assertEquals(roomExpected.getDetails(), roomActual.getDetails());
    }

    @Test
    void givenNewWithBlankLocation_whenSave_thenException() {
        RoomEntity room = new RoomEntity();
        room.setLocation("       ");
        room.setCapacity((short)100);
        room.setDetails("lecture hall");

        assertThrows(RuntimeException.class, () -> roomRepo.save(room));
    }

    @Test
    void givenTwoNewWithSameLocation_whenSave_thenException() {
        RoomEntity room1 = new RoomEntity();
        room1.setLocation("01");
        room1.setCapacity((short)100);
        room1.setDetails("lecture hall");

        RoomEntity room2 = new RoomEntity();
        room2.setLocation(room1.getLocation());
        room2.setCapacity((short)150);
        room2.setDetails("another lecture hall");

        assertDoesNotThrow(() -> roomRepo.save(room1));
        assertThrows(RuntimeException.class, () -> roomRepo.save(room2));
    }

    @Test
    void givenNewWithNegativeCapacity_whenSave_thenException() {
        RoomEntity room = new RoomEntity();
        room.setLocation("01");
        room.setCapacity((short)-10);
        room.setDetails("lecture hall");

        assertThrows(RuntimeException.class, () -> roomRepo.save(room));
    }

    @Test
    void givenNewWithNullDetails_whenSave_thenSuccess() {
        RoomEntity room = new RoomEntity();
        room.setLocation("01");
        room.setCapacity((short)100);
        room.setDetails(null);

        assertDoesNotThrow(() -> roomRepo.save(room));
    }

    @Test
    void givenNewWithBlankDetails_whenSave_thenException() {
        RoomEntity room = new RoomEntity();
        room.setLocation("01");
        room.setCapacity((short)100);
        room.setDetails("     ");

        assertThrows(RuntimeException.class, () -> roomRepo.save(room));
    }


}