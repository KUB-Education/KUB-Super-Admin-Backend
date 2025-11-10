package education.kub.backend.ce.domain.term.repository;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.repository.StudyFieldRepository;
import education.kub.backend.ce.domain.term.entity.TermEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TermRepositoryTest {
    @Autowired
    private StudyFieldRepository studyFieldRepo;

    @Autowired
    private SpecialtyRepository specialtyRepo;

    @Autowired
    private EducationalProgramRepository educationalProgramRepo;

    @Autowired
    private TermRepository termRepo;

    private StudyFieldEntity studyField;
    private SpecialtyEntity specialty;
    private EducationalProgramEntity educationalProgram;

    @BeforeEach
    void setUp() {
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

        termRepo.deleteAll();
    }

    @AfterEach
    void tearDown() {
        educationalProgramRepo.delete(educationalProgram);
        specialtyRepo.delete(specialty);
        studyFieldRepo.delete(studyField);
    }


    @Test
    void givenNew_whenSave_thenSuccess() {
        TermEntity termExpected = new TermEntity();
        termExpected.setEducationalProgram(educationalProgram);
        termExpected.setNumber((short) 5);

        assertDoesNotThrow(() -> termRepo.save(termExpected));

        TermEntity termActual = termRepo.findById(termExpected.getId()).get();

        assertEquals(termExpected, termActual);
        assertEquals(termExpected.getEducationalProgram(), termActual.getEducationalProgram());
        assertEquals(termExpected.getNumber(), termActual.getNumber());
    }

    @Test
    void givenNewWithNegativeNumber_whenSave_thenException() {
        TermEntity term = new TermEntity();
        term.setEducationalProgram(educationalProgram);
        term.setNumber((short) -5);

        assertThrows(RuntimeException.class, () -> termRepo.save(term));
    }
}