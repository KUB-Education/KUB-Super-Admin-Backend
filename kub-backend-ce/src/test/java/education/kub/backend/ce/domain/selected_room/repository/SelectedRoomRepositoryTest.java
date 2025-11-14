package education.kub.backend.ce.domain.selected_room.repository;

import education.kub.backend.ce.domain.group.entity.GroupEntity;
import education.kub.backend.ce.domain.group.repository.GroupRepository;
import education.kub.backend.ce.domain.room.entity.RoomEntity;
import education.kub.backend.ce.domain.room.repository.RoomRepository;
import education.kub.backend.ce.domain.selected_room.entity.SelectedRoomEntity;
import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;
import education.kub.backend.ce.domain.timetable.repository.TimetableRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SelectedRoomRepositoryTest {
    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private TimetableRepository timetableRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private SelectedRoomRepository selectedRoomRepo;

    private GroupEntity group;
    private TimetableEntity timetable;
    private RoomEntity room;

    @BeforeEach
    void setUp() {
        // create group
        group = new GroupEntity();
        group.setName("Group");
        group.setCreatedAt(Instant.now());
        groupRepo.save(group);

        // create timetable
        timetable = new TimetableEntity();
        timetable.setName("Timetable");
        timetable.setGroup(group);
        timetable.setStatus(TimetableEntity.Status.PUBLISHED);
        timetable.setTimeStart(Instant.now());
        timetable.setTimeEnd(Instant.now());
        timetableRepo.save(timetable);

        // create room
        room = new RoomEntity();
        room.setCapacity((short) 100);
        room.setLocation("some location");
        room.setDescription("some description");
        roomRepo.save(room);
    }

    @AfterEach
    void tearDown() {
        roomRepo.delete(room);
        timetableRepo.delete(timetable);
        groupRepo.delete(group);
    }


    @Test
    void givenNew_whenSave_thenSuccess() {
        SelectedRoomEntity srExpected = new SelectedRoomEntity();
        srExpected.setTimetable(timetable);
        srExpected.setRoom(room);

        assertDoesNotThrow(() -> selectedRoomRepo.save(srExpected));

        SelectedRoomEntity srActual = selectedRoomRepo.findById(srExpected.getId()).get();

        assertEquals(srExpected, srActual);
        assertEquals(srExpected.getTimetable(), srActual.getTimetable());
        assertEquals(srExpected.getRoom(), srActual.getRoom());
    }

    @Test
    void givenTwoNewWithSameTimetableAndRoom_whenSave_thenException() {
        SelectedRoomEntity sr1 = new SelectedRoomEntity();
        sr1.setTimetable(timetable);
        sr1.setRoom(room);
        assertDoesNotThrow(() -> selectedRoomRepo.save(sr1));

        SelectedRoomEntity sr2 = new SelectedRoomEntity();
        sr2.setTimetable(timetable);
        sr2.setRoom(room);
        assertThrows(RuntimeException.class, () -> selectedRoomRepo.save(sr2));
    }
}