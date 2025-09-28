package education.kub.backend.ce.domain.selected_lecturer.repository;

import education.kub.backend.ce.domain.group.domain.GroupEntity;
import education.kub.backend.ce.domain.group.repository.GroupRepository;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;
import education.kub.backend.ce.domain.selected_lecturer.domain.SelectedLecturerEntity;
import education.kub.backend.ce.domain.timetable.domain.TimetableEntity;
import education.kub.backend.ce.domain.timetable.repository.TimetableRepository;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SelectedLecturerRepositoryTest {
    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private TimetableRepository timetableRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LecturerRepository lecturerRepo;

    @Autowired
    private SelectedLecturerRepository selectedLecturerRepo;

    private GroupEntity group;
    private TimetableEntity timetable;
    private UserEntity user;
    private LecturerEntity lecturer;


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

        // create user
        user = new UserEntity();
        user.setEmail("hello@email.com");
        user.setFirstName("John");
        user.setLastName("John");
        user.setMiddleName("John");
        user.setStatus(UserEntity.Status.ACTIVATED);
        userRepo.save(user);

        // create lecturer
        lecturer = new LecturerEntity();
        lecturer.setUser(user);
        lecturerRepo.save(lecturer);
    }

    @AfterEach
    void tearDown() {
        lecturerRepo.delete(lecturer);
        userRepo.delete(user);
        timetableRepo.delete(timetable);
        groupRepo.delete(group);
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        SelectedLecturerEntity slExpected = new SelectedLecturerEntity();
        slExpected.setTimetable(timetable);
        slExpected.setLecturer(lecturer);

        assertDoesNotThrow(() -> selectedLecturerRepo.save(slExpected));

        SelectedLecturerEntity slActual = selectedLecturerRepo.findById(slExpected.getId()).get();

        assertEquals(slExpected, slActual);
        assertEquals(slExpected.getTimetable(), slActual.getTimetable());
        assertEquals(slExpected.getLecturer(), slActual.getLecturer());
    }

    @Test
    void givenTwoNewWithSameTimetableAndLecturer_whenSave_thenException() {
        SelectedLecturerEntity sl1 = new SelectedLecturerEntity();
        sl1.setTimetable(timetable);
        sl1.setLecturer(lecturer);
        assertDoesNotThrow(() -> selectedLecturerRepo.save(sl1));

        SelectedLecturerEntity sl2 = new SelectedLecturerEntity();
        sl2.setTimetable(timetable);
        sl2.setLecturer(lecturer);
        assertThrows(RuntimeException.class, () -> selectedLecturerRepo.save(sl2));
    }

}