package education.kub.backend.ce.domain.educational_program.repository;

import education.kub.backend.ce.domain.educational_program.domain.EducationalProgramEntity;
import education.kub.backend.ce.domain.specialty.domain.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import education.kub.backend.ce.domain.study_field.domain.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.repository.StudyFieldRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EducationalProgramRepositoryTest {
    @Autowired
    private StudyFieldRepository studyFieldRepo;

    @Autowired
    private SpecialtyRepository specialtyRepo;

    @Autowired
    private EducationalProgramRepository educationalProgramRepo;

    private StudyFieldEntity studyField;
    private SpecialtyEntity specialty;


    @BeforeEach
    void setUp() {
        studyField = new StudyFieldEntity();
        studyField.setCode("some code");
        studyField.setName("some name");
        studyFieldRepo.save(studyField);

        specialty = new SpecialtyEntity();
        specialty.setCode("some code");
        specialty.setName("some name");
        specialty.setStudyField(studyField);
        specialtyRepo.save(specialty);


        educationalProgramRepo.deleteAll();
    }

    @AfterEach
    void tearDown() {
        specialtyRepo.delete(specialty);
        studyFieldRepo.delete(studyField);
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        EducationalProgramEntity educationalProgramExpected = new EducationalProgramEntity();
        educationalProgramExpected.setName("some name");
        educationalProgramExpected.setSpecialty(specialty);
        educationalProgramExpected.setDuration((short)100);
        educationalProgramExpected.setDegreeType(EducationalProgramEntity.DegreeType.MASTER);
        educationalProgramExpected.setStudyForm(EducationalProgramEntity.StudyForm.FULL_TIME);

        assertDoesNotThrow(() -> educationalProgramRepo.save(educationalProgramExpected));

        var educationalProgramActual = educationalProgramRepo.findById(educationalProgramExpected.getId()).get();

        assertEquals(educationalProgramExpected, educationalProgramActual);
        assertEquals(educationalProgramExpected.getSpecialty(), educationalProgramActual.getSpecialty());
        assertEquals(educationalProgramExpected.getName(), educationalProgramActual.getName());
        assertEquals(educationalProgramExpected.getDegreeType(), educationalProgramActual.getDegreeType());
        assertEquals(educationalProgramExpected.getStudyForm(), educationalProgramActual.getStudyForm());
        assertEquals(educationalProgramExpected.getDuration(), educationalProgramActual.getDuration());
    }

    @Test
    void givenNewWithNonUniqueName_whenSave_thenException() {
        EducationalProgramEntity educationalProgram1 = new EducationalProgramEntity();
        educationalProgram1.setName("some name");
        educationalProgram1.setSpecialty(specialty);
        educationalProgram1.setDuration((short)100);
        educationalProgram1.setDegreeType(EducationalProgramEntity.DegreeType.MASTER);
        educationalProgram1.setStudyForm(EducationalProgramEntity.StudyForm.FULL_TIME);

        EducationalProgramEntity educationalProgram2 = new EducationalProgramEntity();
        educationalProgram2.setName(educationalProgram1.getName());
        educationalProgram2.setSpecialty(specialty);
        educationalProgram2.setDuration((short)1000);
        educationalProgram2.setDegreeType(EducationalProgramEntity.DegreeType.BACHELOR);
        educationalProgram2.setStudyForm(EducationalProgramEntity.StudyForm.ONLINE);

        assertDoesNotThrow(() -> educationalProgramRepo.save(educationalProgram1));
        assertThrows(RuntimeException.class, () -> educationalProgramRepo.save(educationalProgram2));
    }

    @Test
    void givenNewWithBlankName_whenSave_thenException() {
        EducationalProgramEntity educationalProgram = new EducationalProgramEntity();
        educationalProgram.setName("      ");
        educationalProgram.setSpecialty(specialty);
        educationalProgram.setDuration((short)100);
        educationalProgram.setDegreeType(EducationalProgramEntity.DegreeType.MASTER);
        educationalProgram.setStudyForm(EducationalProgramEntity.StudyForm.FULL_TIME);

        assertThrows(RuntimeException.class, () -> educationalProgramRepo.save(educationalProgram));
    }

    @Test
    void givenNewWithNegativeDuration_whenSave_thenException() {
        EducationalProgramEntity educationalProgram = new EducationalProgramEntity();
        educationalProgram.setName("some name");
        educationalProgram.setSpecialty(specialty);
        educationalProgram.setDuration((short)-10);
        educationalProgram.setDegreeType(EducationalProgramEntity.DegreeType.MASTER);
        educationalProgram.setStudyForm(EducationalProgramEntity.StudyForm.FULL_TIME);

        assertThrows(RuntimeException.class, () -> educationalProgramRepo.save(educationalProgram));
    }

    @Test
    void givenEducationalPrograms_whenSaveForSpecialty_AppearInSpecialty() {
        EducationalProgramEntity educationalProgram1 = new EducationalProgramEntity();
        educationalProgram1.setName("some name 1");
        educationalProgram1.setSpecialty(specialty);
        educationalProgram1.setDuration((short)100);
        educationalProgram1.setDegreeType(EducationalProgramEntity.DegreeType.MASTER);
        educationalProgram1.setStudyForm(EducationalProgramEntity.StudyForm.FULL_TIME);

        EducationalProgramEntity educationalProgram2 = new EducationalProgramEntity();
        educationalProgram2.setName("some name 2");
        educationalProgram2.setSpecialty(specialty);
        educationalProgram2.setDuration((short)1000);
        educationalProgram2.setDegreeType(EducationalProgramEntity.DegreeType.BACHELOR);
        educationalProgram2.setStudyForm(EducationalProgramEntity.StudyForm.ONLINE);

        List<EducationalProgramEntity> expectedEducationalPrograms = List.of(educationalProgram1, educationalProgram2);

        specialty.getEducationalPrograms().addAll(expectedEducationalPrograms);

        assertDoesNotThrow(() -> educationalProgramRepo.save(educationalProgram1));
        assertDoesNotThrow(() -> educationalProgramRepo.save(educationalProgram2));
        assertDoesNotThrow(() -> specialtyRepo.save(specialty));


        SpecialtyEntity specialtyActual = specialtyRepo.findById(specialty.getId()).get();
        assertEquals(expectedEducationalPrograms, specialtyActual.getEducationalPrograms());
    }

}