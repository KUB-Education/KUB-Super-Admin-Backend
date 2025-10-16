package education.kub.backend.ce.domain.student_educational_program.repository;

import education.kub.backend.ce.domain.educational_program.domain.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import education.kub.backend.ce.domain.student.entity.StudentEntity;
import education.kub.backend.ce.domain.student.repository.StudentRepository;
import education.kub.backend.ce.domain.student_educational_program.domain.StudentEducationalProgramEntity;
import education.kub.backend.ce.domain.study_field.domain.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.repository.StudyFieldRepository;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
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
class StudentEducationalProgramRepositoryTest {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StudyFieldRepository studyFieldRepo;

    @Autowired
    private SpecialtyRepository specialtyRepo;

    @Autowired
    private EducationalProgramRepository educationalProgramRepo;

    @Autowired
    private StudentEducationalProgramRepository studentEducationalProgramRepo;

    private UserEntity user;
    private StudentEntity student;
    private StudyFieldEntity studyField;
    private SpecialtyEntity specialty;
    private EducationalProgramEntity educationalProgram;
    @Autowired
    private StudentEducationalProgramRepository studentEducationalProgramRepository;


    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMiddleName("Smith");
        user.setEmail("someemail@someemail.com");
        userRepo.save(user);

        student = new StudentEntity();
        student.setUser(user);
        studentRepo.save(student);


        studyField = new StudyFieldEntity();
        studyField.setCode("12");
        studyField.setName("IT");
        studyFieldRepo.save(studyField);

        specialty = new SpecialtyEntity();
        specialty.setCode("121");
        specialty.setName("Software Engineering");
        specialty.setStudyField(studyField);
        specialtyRepo.save(specialty);

        educationalProgram = new EducationalProgramEntity();
        educationalProgram.setName("Software Engineering");
        educationalProgram.setSpecialty(specialty);
        educationalProgram.setDuration((short)100);
        educationalProgram.setDegreeType(EducationalProgramEntity.DegreeType.MASTER);
        educationalProgram.setStudyForm(EducationalProgramEntity.StudyForm.FULL_TIME);
        educationalProgramRepo.save(educationalProgram);


        studentEducationalProgramRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        studentRepo.delete(student);
        userRepo.delete(user);

        educationalProgramRepo.delete(educationalProgram);
        specialtyRepo.delete(specialty);
        studyFieldRepo.delete(studyField);
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        var sepExpected = new StudentEducationalProgramEntity();
        sepExpected.setStudent(student);
        sepExpected.setEducationalProgram(educationalProgram);
        sepExpected.setStatus(StudentEducationalProgramEntity.Status.ACTIVE);
        sepExpected.setTuition(StudentEducationalProgramEntity.Tuition.BUDGET);
        sepExpected.setStartDate(Instant.now());
        sepExpected.setFinishDate(Instant.now());

        assertDoesNotThrow(() -> studentEducationalProgramRepo.save(sepExpected));

        var sepActual = studentEducationalProgramRepo.findById(sepExpected.getId()).get();
        assertEquals(sepExpected, sepActual);
        assertEquals(sepExpected.getStudent(), sepActual.getStudent());
        assertEquals(sepExpected.getEducationalProgram(), sepActual.getEducationalProgram());
        assertEquals(sepExpected.getStartDate(), sepActual.getStartDate());
        assertEquals(sepExpected.getFinishDate(), sepActual.getFinishDate());
        assertEquals(sepExpected.getTuition(), sepActual.getTuition());
        assertEquals(sepExpected.getStatus(), sepActual.getStatus());
    }

    @Test
    void givenNewWithSameStudentAndEducationalProgram_whenSave_thenException() {
        var sep1 = new StudentEducationalProgramEntity();
        sep1.setStudent(student);
        sep1.setEducationalProgram(educationalProgram);
        sep1.setStatus(StudentEducationalProgramEntity.Status.ACTIVE);
        sep1.setTuition(StudentEducationalProgramEntity.Tuition.BUDGET);
        sep1.setStartDate(Instant.now());
        sep1.setFinishDate(Instant.now());

        var sep2 = new StudentEducationalProgramEntity();
        sep2.setStudent(student);
        sep2.setEducationalProgram(educationalProgram);
        sep2.setStatus(StudentEducationalProgramEntity.Status.ACTIVE);
        sep2.setTuition(StudentEducationalProgramEntity.Tuition.BUDGET);
        sep2.setStartDate(Instant.now());
        sep2.setFinishDate(Instant.now());

        assertDoesNotThrow(() -> studentEducationalProgramRepo.save(sep1));
        assertThrows(RuntimeException.class, () -> studentEducationalProgramRepo.save(sep2));
    }


}