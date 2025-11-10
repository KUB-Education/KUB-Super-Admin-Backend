package education.kub.backend.ce.domain.timetable.repository;

import education.kub.backend.ce.domain.group.entity.GroupEntity;
import education.kub.backend.ce.domain.group.repository.GroupRepository;
import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;
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
class TimetableRepositoryTest {
    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private TimetableRepository timetableRepo;

    private GroupEntity group;


    @BeforeEach
    void setUp(){
        group = new GroupEntity();
        group.setName("Group");
        group.setCreatedAt(Instant.now());
        groupRepo.save(group);
    }

    @AfterEach
    void tearDown() {
        groupRepo.delete(group);
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        TimetableEntity timetableExpected = new TimetableEntity();
        timetableExpected.setName("Timetable");
        timetableExpected.setGroup(group);
        timetableExpected.setStatus(TimetableEntity.Status.PUBLISHED);
        timetableExpected.setTimeStart(Instant.now());
        timetableExpected.setTimeEnd(Instant.now());

        assertDoesNotThrow(() -> timetableRepo.save(timetableExpected));

        TimetableEntity timetableActual = timetableRepo.findById(timetableExpected.getId()).get();

        assertEquals(timetableExpected, timetableActual);
        assertEquals(timetableExpected.getName(), timetableActual.getName());
        assertEquals(timetableExpected.getGroup(), timetableActual.getGroup());
        assertEquals(timetableExpected.getTimeStart(), timetableActual.getTimeStart());
        assertEquals(timetableExpected.getTimeEnd(), timetableActual.getTimeEnd());
        assertEquals(timetableExpected.getStatus(), timetableActual.getStatus());
    }

    @Test
    void givenNewWithBlankName_whenSave_thenException() {
        TimetableEntity timetable = new TimetableEntity();
        timetable.setName("         ");
        timetable.setGroup(group);
        timetable.setStatus(TimetableEntity.Status.PUBLISHED);
        timetable.setTimeStart(Instant.now());
        timetable.setTimeEnd(Instant.now());

        assertThrows(RuntimeException.class, () -> timetableRepo.save(timetable));
    }
}