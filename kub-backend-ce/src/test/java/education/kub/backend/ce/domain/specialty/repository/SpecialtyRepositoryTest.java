package education.kub.backend.ce.domain.specialty.repository;

import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
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
class SpecialtyRepositoryTest {
    @Autowired
    private StudyFieldRepository studyFieldRepo;

    @Autowired
    private SpecialtyRepository specialtyRepo;

    private StudyFieldEntity studyField;

    @BeforeEach
    void setUp() {
        studyField = new StudyFieldEntity();
        studyField.setCode("some code");
        studyField.setName("some name");
        studyFieldRepo.save(studyField);

        specialtyRepo.deleteAll();
    }

    @AfterEach
    void tearDown() {
        studyFieldRepo.delete(studyField);
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        SpecialtyEntity specialtyExpected = new SpecialtyEntity();
        specialtyExpected.setStudyField(studyField);
        specialtyExpected.setCode("121");
        specialtyExpected.setName("Software engineering");

        assertDoesNotThrow(() -> specialtyRepo.save(specialtyExpected));

        SpecialtyEntity specialtyActual = specialtyRepo.findById(specialtyExpected.getId()).get();

        assertEquals(specialtyExpected, specialtyActual);
        assertEquals(specialtyExpected.getStudyField(), specialtyActual.getStudyField());
        assertEquals(specialtyExpected.getCode(), specialtyActual.getCode());
        assertEquals(specialtyExpected.getName(), specialtyActual.getName());
    }

    @Test
    void givenNewWithNonUniqueCode_whenSave_thenException() {
        SpecialtyEntity specialty1 = new SpecialtyEntity();
        specialty1.setStudyField(studyField);
        specialty1.setCode("121");
        specialty1.setName("Software engineering");

        SpecialtyEntity specialty2 = new SpecialtyEntity();
        specialty2.setStudyField(specialty1.getStudyField());
        specialty2.setCode(specialty1.getCode());
        specialty2.setName(specialty1.getName());

        assertDoesNotThrow(() -> specialtyRepo.save(specialty1));
        assertThrows(RuntimeException.class, () -> specialtyRepo.save(specialty2));
    }

    @Test
    void givenNewWithEmptyCode_whenSave_thenException() {
        SpecialtyEntity specialty = new SpecialtyEntity();
        specialty.setStudyField(studyField);
        specialty.setCode("");
        specialty.setName("Software engineering");

        assertThrows(RuntimeException.class, () -> specialtyRepo.save(specialty));
    }

    @Test
    void givenNewWithEmptyName_whenSave_thenException() {
        SpecialtyEntity specialty = new SpecialtyEntity();
        specialty.setStudyField(studyField);
        specialty.setCode("121");
        specialty.setName("");

        assertThrows(RuntimeException.class, () -> specialtyRepo.save(specialty));
    }

    @Test
    void givenSpecialties_whenSaveForStudyField_AppearsImStudyField() {
        SpecialtyEntity specialty1 = new SpecialtyEntity();
        specialty1.setStudyField(studyField);
        specialty1.setCode("121");
        specialty1.setName("Software engineering");

        SpecialtyEntity specialty2 = new SpecialtyEntity();
        specialty2.setStudyField(studyField);
        specialty2.setCode("122");
        specialty2.setName("Computer science");

        List<SpecialtyEntity> expectedSpecialties = List.of(specialty1, specialty2);

        studyField.getSpecialties().addAll(expectedSpecialties);

        assertDoesNotThrow(() -> specialtyRepo.save(specialty1));
        assertDoesNotThrow(() -> specialtyRepo.save(specialty2));
        assertDoesNotThrow(() -> studyFieldRepo.save(studyField));

        StudyFieldEntity studyFieldActual = studyFieldRepo.findById(studyField.getId()).get();
        assertEquals(expectedSpecialties, studyFieldActual.getSpecialties());
    }
}