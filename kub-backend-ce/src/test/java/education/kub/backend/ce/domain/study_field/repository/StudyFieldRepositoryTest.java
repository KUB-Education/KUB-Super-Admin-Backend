package education.kub.backend.ce.domain.study_field.repository;

import education.kub.backend.ce.domain.study_field.domain.StudyFieldEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudyFieldRepositoryTest {
    @Autowired
    private StudyFieldRepository studyFieldRepo;

    @BeforeEach
    void setUp() {
        studyFieldRepo.deleteAll();
        studyFieldRepo.flush();
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        StudyFieldEntity studyFieldExpected = new StudyFieldEntity();
        studyFieldExpected.setCode("121");
        studyFieldExpected.setName("Software engineering");

        assertDoesNotThrow(() -> studyFieldRepo.save(studyFieldExpected));

        StudyFieldEntity studyFieldActual = studyFieldRepo.findById(studyFieldExpected.getId()).get();

        assertEquals(studyFieldExpected, studyFieldActual);
        assertEquals(studyFieldExpected.getCode(), studyFieldActual.getCode());
        assertEquals(studyFieldExpected.getName(), studyFieldActual.getName());
    }

    @Test
    void givenNewWithNonUniqueCode_whenSave_thenException() {
        StudyFieldEntity studyField1 = new StudyFieldEntity();
        studyField1.setCode("121");
        studyField1.setName("Software engineering");

        StudyFieldEntity studyField2 = new StudyFieldEntity();
        studyField2.setCode(studyField1.getCode());
        studyField2.setName(studyField1.getName());

        assertDoesNotThrow(() -> studyFieldRepo.save(studyField1));
        assertThrows(RuntimeException.class, () -> studyFieldRepo.save(studyField2));
    }

    @Test
    void givenNewWithEmptyCode_whenSave_thenException() {
        StudyFieldEntity studyField = new StudyFieldEntity();
        studyField.setCode("");
        studyField.setName("Software engineering");

        assertThrows(RuntimeException.class, () -> studyFieldRepo.save(studyField));
    }

    @Test
    void givenNewWithEmptyName_whenSave_thenException() {
        StudyFieldEntity studyField = new StudyFieldEntity();
        studyField.setCode("121");
        studyField.setName("");

        assertThrows(RuntimeException.class, () -> studyFieldRepo.save(studyField));
    }
}