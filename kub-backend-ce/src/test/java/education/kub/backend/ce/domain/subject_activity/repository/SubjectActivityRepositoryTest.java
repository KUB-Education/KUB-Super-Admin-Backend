package education.kub.backend.ce.domain.subject_activity.repository;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.repository.StudyFieldRepository;
import education.kub.backend.ce.domain.subject.entity.SubjectEntity;
import education.kub.backend.ce.domain.subject.repository.SubjectRepository;
import education.kub.backend.ce.domain.subject_activity.entity.SubjectActivityEntity;
import education.kub.backend.ce.domain.term.entity.TermEntity;
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
class SubjectActivityRepositoryTest {
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

    private StudyFieldEntity studyField;
    private SpecialtyEntity specialty;
    private EducationalProgramEntity educationalProgram;
    private TermEntity term;
    private SubjectEntity subject;

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

        subject = new SubjectEntity();
        subject.setTerm(term);
        subject.setName("Software Engineering");
        subject.setType(SubjectEntity.Type.MANDATORY);
        subjectRepo.save(subject);

        subjectActivityRepo.deleteAll();
    }

    @AfterEach
    void tearDown() {
        subjectRepo.delete(subject);
        termRepo.delete(term);
        educationalProgramRepo.delete(educationalProgram);
        specialtyRepo.delete(specialty);
        studyFieldRepo.delete(studyField);
    }


    @Test
    void givenNew_whenSave_thenSuccess() {
        SubjectActivityEntity saeExpected = new SubjectActivityEntity();
        saeExpected.setSubject(subject);
        saeExpected.setType(SubjectActivityEntity.Type.LABORATORY);
        saeExpected.setAcademicHours((short)210);

        assertDoesNotThrow(() -> subjectActivityRepo.save(saeExpected));

        SubjectActivityEntity saeActual = subjectActivityRepo.findById(saeExpected.getId()).get();

        assertEquals(saeExpected, saeActual);
        assertEquals(saeExpected.getSubject(), saeActual.getSubject());
        assertEquals(saeExpected.getType(), saeActual.getType());
        assertEquals(saeExpected.getAcademicHours(), saeActual.getAcademicHours());
    }

    @Test
    void givenNewWithNegativeAcademicHours_whenSave_thenException() {
        SubjectActivityEntity sae = new SubjectActivityEntity();
        sae.setSubject(subject);
        sae.setType(SubjectActivityEntity.Type.LABORATORY);
        sae.setAcademicHours((short)-210);

        assertThrows(RuntimeException.class, () -> subjectActivityRepo.save(sae));
    }


}