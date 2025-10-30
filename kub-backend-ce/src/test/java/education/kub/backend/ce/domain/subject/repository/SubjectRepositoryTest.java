package education.kub.backend.ce.domain.subject.repository;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.repository.StudyFieldRepository;
import education.kub.backend.ce.domain.subject.domain.SubjectEntity;
import education.kub.backend.ce.domain.term.domain.TermEntity;
import education.kub.backend.ce.domain.term.repository.TermRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SubjectRepositoryTest {
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

    private StudyFieldEntity studyField;
    private SpecialtyEntity specialty;
    private EducationalProgramEntity educationalProgram;
    private TermEntity term;

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

        term = new TermEntity();
        term.setEducationalProgram(educationalProgram);
        term.setNumber((short) 5);
        termRepo.save(term);

        subjectRepo.deleteAll();
    }

    @AfterEach
    void tearDown() {
        termRepo.delete(term);
        educationalProgramRepo.delete(educationalProgram);
        specialtyRepo.delete(specialty);
        studyFieldRepo.delete(studyField);
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        SubjectEntity subjectExpected = new SubjectEntity();
        subjectExpected.setTerm(term);
        subjectExpected.setName("Software Engineering");
        subjectExpected.setType(SubjectEntity.Type.MANDATORY);

        assertDoesNotThrow(() -> subjectRepo.save(subjectExpected));

        SubjectEntity subjectActual = subjectRepo.findById(subjectExpected.getId()).get();

        assertEquals(subjectExpected, subjectActual);
        assertEquals(subjectExpected.getTerm(), subjectActual.getTerm());
        assertEquals(subjectExpected.getName(), subjectActual.getName());
        assertEquals(subjectExpected.getType(), subjectActual.getType());
    }

    @Test
    void givenNewWithEmptyName_whenSave_thenException() {
        SubjectEntity subjectExpected = new SubjectEntity();
        subjectExpected.setTerm(term);
        subjectExpected.setName("");
        subjectExpected.setType(SubjectEntity.Type.MANDATORY);

        assertThrows(RuntimeException.class, () -> subjectRepo.save(subjectExpected));
    }

}