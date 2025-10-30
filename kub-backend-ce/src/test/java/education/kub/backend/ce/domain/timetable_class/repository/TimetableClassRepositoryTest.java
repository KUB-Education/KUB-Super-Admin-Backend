package education.kub.backend.ce.domain.timetable_class.repository;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import education.kub.backend.ce.domain.group.domain.GroupEntity;
import education.kub.backend.ce.domain.group.repository.GroupRepository;
import education.kub.backend.ce.domain.room.domain.RoomEntity;
import education.kub.backend.ce.domain.room.repository.RoomRepository;
import education.kub.backend.ce.domain.selected_room.domain.SelectedRoomEntity;
import education.kub.backend.ce.domain.selected_room.repository.SelectedRoomRepository;
import education.kub.backend.ce.domain.selected_subject_activity.domain.SelectedSubjectActivityEntity;
import education.kub.backend.ce.domain.selected_subject_activity.repository.SelectedSubjectActivityRepository;
import education.kub.backend.ce.domain.selected_subject_activity_group.domain.SelectedSubjectActivityGroupEntity;
import education.kub.backend.ce.domain.selected_subject_activity_group.repository.SelectedSubjectActivityGroupRepository;
import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.repository.StudyFieldRepository;
import education.kub.backend.ce.domain.subject.domain.SubjectEntity;
import education.kub.backend.ce.domain.subject.repository.SubjectRepository;
import education.kub.backend.ce.domain.subject_activity.domain.SubjectActivityEntity;
import education.kub.backend.ce.domain.subject_activity.repository.SubjectActivityRepository;
import education.kub.backend.ce.domain.term.domain.TermEntity;
import education.kub.backend.ce.domain.term.repository.TermRepository;
import education.kub.backend.ce.domain.timetable.domain.TimetableEntity;
import education.kub.backend.ce.domain.timetable.repository.TimetableRepository;
import education.kub.backend.ce.domain.timetable_class.entity.TimetableClassEntity;
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
class TimetableClassRepositoryTest {
    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private TimetableRepository timetableRepo;

    @Autowired
    private StudyFieldRepository studyFieldRepo;

    @Autowired
    private SpecialtyRepository specialtyRepo;

    @Autowired
    private EducationalProgramRepository educationalProgramRepo;

    @Autowired
    private TermRepository termRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private SubjectActivityRepository subjectActivityRepo;

    @Autowired
    private SelectedSubjectActivityRepository selectedSubjectActivityRepo;

    @Autowired
    private SelectedSubjectActivityGroupRepository selectedSubjectActivityGroupRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private SelectedRoomRepository selectedRoomRepo;

    @Autowired
    private TimetableClassRepository timetableClassRepo;


    private GroupEntity group;
    private TimetableEntity timetable;
    private StudyFieldEntity studyField;
    private SpecialtyEntity specialty;
    private EducationalProgramEntity educationalProgram;
    private TermEntity term;
    private SubjectEntity subject;
    private SubjectActivityEntity subjectActivity;
    private SelectedSubjectActivityEntity selectedSubjectActivity;
    private SelectedSubjectActivityGroupEntity selectedSubjectActivityGroup;
    private RoomEntity room;
    private SelectedRoomEntity selectedRoom;

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


        // create study field
        studyField = new StudyFieldEntity();
        studyField.setCode("12");
        studyField.setName("IT");
        studyFieldRepo.save(studyField);

        // create specialty
        specialty = new SpecialtyEntity();
        specialty.setCode("121");
        specialty.setName("Software Engineering");
        specialty.setStudyField(studyField);
        specialtyRepo.save(specialty);

        // create educational program
        educationalProgram = new EducationalProgramEntity();
        educationalProgram.setName("Software Engineering");
        educationalProgram.setSpecialty(specialty);
        educationalProgram.setDuration((short)100);
        educationalProgram.setDegreeType(EducationalProgramEntity.DegreeType.MASTER);
        educationalProgram.setStudyForm(EducationalProgramEntity.StudyForm.FULL_TIME);
        educationalProgramRepo.save(educationalProgram);

        // create term
        term = new TermEntity();
        term.setEducationalProgram(educationalProgram);
        term.setNumber((short) 5);
        termRepo.save(term);

        // create subject
        subject = new SubjectEntity();
        subject.setTerm(term);
        subject.setName("Software Engineering");
        subject.setType(SubjectEntity.Type.MANDATORY);
        subjectRepo.save(subject);

        // create subject activity
        subjectActivity = new SubjectActivityEntity();
        subjectActivity.setSubject(subject);
        subjectActivity.setType(SubjectActivityEntity.Type.LABORATORY);
        subjectActivity.setAcademicHours((short)210);
        subjectActivityRepo.save(subjectActivity);

        // create selected subject activity
        selectedSubjectActivity = new SelectedSubjectActivityEntity();
        selectedSubjectActivity.setTimetable(timetable);
        selectedSubjectActivity.setSubjectActivity(subjectActivity);
        selectedSubjectActivityRepo.save(selectedSubjectActivity);

        // create selected subject activity group
        selectedSubjectActivityGroup = new SelectedSubjectActivityGroupEntity();
        selectedSubjectActivityGroup.setSelectedSubjectActivity(selectedSubjectActivity);
        selectedSubjectActivityGroup.setName("some name");
        selectedSubjectActivityGroupRepo.save(selectedSubjectActivityGroup);


        // create room
        room = new RoomEntity();
        room.setLocation("some location");
        room.setCapacity((short)100);
        room.setDetails("some details");
        roomRepo.save(room);

        // create selected room
        selectedRoom = new SelectedRoomEntity();
        selectedRoom.setTimetable(timetable);
        selectedRoom.setRoom(room);
        selectedRoomRepo.save(selectedRoom);
    }

    @AfterEach
    void tearDown() {
        selectedRoomRepo.delete(selectedRoom);
        roomRepo.delete(room);

        selectedSubjectActivityGroupRepo.delete(selectedSubjectActivityGroup);
        selectedSubjectActivityRepo.delete(selectedSubjectActivity);
        subjectActivityRepo.delete(subjectActivity);
        subjectRepo.delete(subject);
        termRepo.delete(term);
        educationalProgramRepo.delete(educationalProgram);
        specialtyRepo.delete(specialty);
        studyFieldRepo.delete(studyField);

        timetableRepo.delete(timetable);
        groupRepo.delete(group);
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        TimetableClassEntity classExpected = new TimetableClassEntity();
        classExpected.setTimetable(timetable);
        classExpected.setSelectedSubjectActivityGroup(selectedSubjectActivityGroup);
        classExpected.setSelectedRoom(selectedRoom);
        classExpected.setType(TimetableClassEntity.Type.ONLINE);
        classExpected.setTimeStart(Instant.now());
        classExpected.setTimeEnd(Instant.now());

        assertDoesNotThrow(() -> timetableClassRepo.save(classExpected));

        TimetableClassEntity classActual = timetableClassRepo.findById(classExpected.getId()).get();

        assertEquals(classExpected, classActual);
        assertEquals(classExpected.getTimetable(), classActual.getTimetable());
        assertEquals(classExpected.getSelectedSubjectActivityGroup(), classActual.getSelectedSubjectActivityGroup());
        assertEquals(classExpected.getSelectedRoom(), classActual.getSelectedRoom());
        assertEquals(classExpected.getType(), classActual.getType());
        assertEquals(classExpected.getTimeStart(), classActual.getTimeStart());
        assertEquals(classExpected.getTimeEnd(), classActual.getTimeEnd());
    }
}