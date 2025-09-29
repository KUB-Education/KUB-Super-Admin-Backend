package education.kub.backend.ce.domain.academic_title.repository;

import education.kub.backend.ce.domain.academic_title.entity.AcademicTitleEntity;
import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AcademicTitleRepositoryTest {
    @Autowired
    private AcademicTitleRepository academicTitleRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LecturerRepository lecturerRepo;

    private UserEntity user;
    private LecturerEntity lecturer;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMiddleName("Smith");
        user.setEmail("someemail");
        userRepo.save(user);

        lecturer = new LecturerEntity();
        lecturer.setUser(user);
        lecturerRepo.save(lecturer);

        academicTitleRepo.deleteAll();
    }

    @AfterEach
    void tearDown() {
        lecturerRepo.delete(lecturer);
        userRepo.delete(user);
    }

    /*
    @Test
    void givenNew_whenSave_thenSuccess() {
        AcademicTitleEntity academicTitleExpected = new AcademicTitleEntity();
        academicTitleExpected.setName("Academic Title name");
        assertDoesNotThrow(() -> academicTitleRepo.save(academicTitleExpected));

        AcademicTitleEntity academicTitleActual = academicTitleRepo.findById(academicTitleExpected.getId()).get();

        assertThat(academicTitleActual).isEqualTo(academicTitleExpected);
        assertThat(academicTitleActual.getName()).isEqualTo(academicTitleExpected.getName());
    }

    // violates unique(name) constraint
    @Test
    void givenTwoNewWithSameNames_whenSave_thenException() {
        AcademicTitleEntity academicTitle1 = new AcademicTitleEntity();
        academicTitle1.setName("Academic Title name");
        AcademicTitleEntity academicTitle2 = new AcademicTitleEntity();
        academicTitle2.setName(academicTitle1.getName());

        assertDoesNotThrow(() -> academicTitleRepo.save(academicTitle1));
        assertThrows(RuntimeException.class, () -> academicTitleRepo.save(academicTitle2));
    }

    // violates name no blank constraint
    @Test
    void givenNewWithBlankName_whenSave_thenException() {
        AcademicTitleEntity academicTitle = new AcademicTitleEntity();
        academicTitle.setName("        ");

        assertThrows(RuntimeException.class, () -> academicTitleRepo.save(academicTitle));
    }

    @Test
    void givenAcademicTitles_whenAddToLecturerFromLecturerSide_thenAppearInLecturerAcademicTitles() {
        AcademicTitleEntity academicTitle1 = new AcademicTitleEntity();
        academicTitle1.setName("DOCTOR_OF_SCIENCE");
        AcademicTitleEntity academicTitle2 = new AcademicTitleEntity();
        academicTitle2.setName("DOCTOR_OF_PHILOSOPHY");

        assertDoesNotThrow(() -> academicTitleRepo.save(academicTitle1));
        assertDoesNotThrow(() -> academicTitleRepo.save(academicTitle2));

        Set<AcademicTitleEntity> expectedAcademicTitles = Set.of(academicTitle1, academicTitle2);

        // add academic titles from side of lecturer
        lecturer.getAcademicTitles().addAll(expectedAcademicTitles);
        lecturerRepo.save(lecturer);

        LecturerEntity lecturerActual = lecturerRepo.findById(lecturer.getId()).get();
        assertEquals(expectedAcademicTitles, lecturerActual.getAcademicTitles());
    }

     */
}