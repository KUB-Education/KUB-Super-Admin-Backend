package education.kub.backend.ce.integration;

import education.kub.backend.ce.domain.educational_program.domain.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import education.kub.backend.ce.domain.group.domain.GroupEntity;
import education.kub.backend.ce.domain.group.repository.GroupRepository;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;
import education.kub.backend.ce.domain.room.domain.RoomEntity;
import education.kub.backend.ce.domain.room.repository.RoomRepository;
import education.kub.backend.ce.domain.selected_lecturer.domain.SelectedLecturerEntity;
import education.kub.backend.ce.domain.selected_lecturer.repository.SelectedLecturerRepository;
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
import education.kub.backend.ce.domain.timetable_class.repository.TimetableClassRepository;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TimetableClassSelectedLecturerTest {
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

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LecturerRepository lecturerRepo;

    @Autowired
    private SelectedLecturerRepository selectedLecturerRepo;


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
    private UserEntity user1;
    private LecturerEntity lecturer1;
    private UserEntity user2;
    private LecturerEntity lecturer2;

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

        // create user1
        user1 = new UserEntity();
        user1.setEmail("user1@email.com");
        user1.setFirstName("name2");
        user1.setLastName("lastname2");
        user1.setMiddleName("middlename2");
        user1.setStatus(UserEntity.Status.ACTIVATED);
        userRepo.save(user1);

        // create lecturer1
        lecturer1 = new LecturerEntity();
        lecturer1.setUser(user1);
        lecturerRepo.save(lecturer1);

        // create user2
        user2 = new UserEntity();
        user2.setEmail("user2@email.com");
        user2.setFirstName("name2");
        user2.setLastName("lastname2");
        user2.setMiddleName("middlename2");
        user2.setStatus(UserEntity.Status.ACTIVATED);
        userRepo.save(user2);

        // create lecturer2
        lecturer2 = new LecturerEntity();
        lecturer2.setUser(user2);
        lecturerRepo.save(lecturer2);
    }

    @AfterEach
    void tearDown() {
        lecturerRepo.delete(lecturer2);
        userRepo.delete(user2);
        lecturerRepo.delete(lecturer1);
        userRepo.delete(user1);

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
        // create class
        TimetableClassEntity classExpected = new TimetableClassEntity();
        classExpected.setTimetable(timetable);
        classExpected.setSelectedSubjectActivityGroup(selectedSubjectActivityGroup);
        classExpected.setSelectedRoom(selectedRoom);
        classExpected.setType(TimetableClassEntity.Type.ONLINE);
        classExpected.setTimeStart(Instant.now());
        classExpected.setTimeEnd(Instant.now());
        timetableClassRepo.save(classExpected);

        // create selected lecturer 1
        SelectedLecturerEntity sLecturer1 = new SelectedLecturerEntity();
        sLecturer1.setLecturer(lecturer1);
        sLecturer1.setTimetable(timetable);
        selectedLecturerRepo.save(sLecturer1);

        // create selected lecturer 2
        SelectedLecturerEntity sLecturer2 = new SelectedLecturerEntity();
        sLecturer2.setLecturer(lecturer2);
        sLecturer2.setTimetable(timetable);
        selectedLecturerRepo.save(sLecturer2);

        // bind selected lecturers to class
        classExpected.addSelectedLecturer(sLecturer1);
        classExpected.addSelectedLecturer(sLecturer2);
        timetableClassRepo.save(classExpected);
        selectedLecturerRepo.save(sLecturer1);
        selectedLecturerRepo.save(sLecturer2);


        // check data
        TimetableClassEntity classActual = timetableClassRepo.findById(classExpected.getId()).get();
        SelectedLecturerEntity sLecturer1Actual = selectedLecturerRepo.findById(sLecturer1.getId()).get();
        SelectedLecturerEntity sLecturer2Actual = selectedLecturerRepo.findById(sLecturer2.getId()).get();

        assertEquals(classExpected.getSelectedLecturers(), classActual.getSelectedLecturers());
        assertEquals(sLecturer1.getClasses(), sLecturer1Actual.getClasses());
        assertEquals(sLecturer2.getClasses(), sLecturer2Actual.getClasses());
    }
}
