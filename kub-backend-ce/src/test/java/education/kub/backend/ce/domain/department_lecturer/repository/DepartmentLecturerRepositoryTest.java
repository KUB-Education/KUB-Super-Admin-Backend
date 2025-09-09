package education.kub.backend.ce.domain.department_lecturer.repository;

import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.repository.DepartmentRepository;
import education.kub.backend.ce.domain.department_lecturer.entity.DepartmentLecturerEntity;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;
import education.kub.backend.ce.domain.student.entity.StudentEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DepartmentLecturerRepositoryTest {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LecturerRepository lecturerRepo;

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private DepartmentLecturerRepository departmentLecturerRepo;

    private UserEntity user;
    private LecturerEntity lecturer;
    private DepartmentEntity department;

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

        department = new DepartmentEntity();
        department.setName("Some department name");
        departmentRepo.save(department);
    }

    @AfterEach
    void tearDown() {
        lecturerRepo.delete(lecturer);
        userRepo.delete(user);
        departmentRepo.delete(department);
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        var departmentLecturerExpected = new DepartmentLecturerEntity();
        departmentLecturerExpected.setDepartment(department);
        departmentLecturerExpected.setLecturer(lecturer);
        departmentLecturerExpected.setStatus(DepartmentLecturerEntity.Status.ACTIVE);
        departmentLecturerExpected.setPosition(DepartmentLecturerEntity.Position.PROFESSOR);
        departmentLecturerRepo.save(departmentLecturerExpected);

        // check is each field is ok
        var departmentLecturerActual = departmentLecturerRepo.findById(departmentLecturerExpected.getId()).orElse(null);
        assertThat(departmentLecturerActual).isEqualTo(departmentLecturerExpected);
        assertThat(departmentLecturerActual.getLecturer()).isEqualTo(departmentLecturerExpected.getLecturer());
        assertThat(departmentLecturerActual.getDepartment()).isEqualTo(departmentLecturerExpected.getDepartment());
        assertThat(departmentLecturerActual.getStatus()).isEqualTo(departmentLecturerExpected.getStatus());
        assertThat(departmentLecturerActual.getPosition()).isEqualTo(departmentLecturerExpected.getPosition());
    }

    // unique(lecturer_id, department_id) constraint is violated
    @Test
    void givenTwoNewWithSameLecturerAndDepartment_whenSave_thenException() {
        var departmentLecturer1 = new DepartmentLecturerEntity();
        departmentLecturer1.setDepartment(department);
        departmentLecturer1.setLecturer(lecturer);
        departmentLecturer1.setStatus(DepartmentLecturerEntity.Status.ACTIVE);
        departmentLecturer1.setPosition(DepartmentLecturerEntity.Position.PROFESSOR);

        var departmentLecturer2 = new DepartmentLecturerEntity();
        departmentLecturer2.setDepartment(department);
        departmentLecturer2.setLecturer(lecturer);
        departmentLecturer2.setStatus(DepartmentLecturerEntity.Status.INACTIVE);
        departmentLecturer2.setPosition(DepartmentLecturerEntity.Position.ASSOCIATE_PROFESSOR);

        assertDoesNotThrow(() -> departmentLecturerRepo.save(departmentLecturer1));
        assertThrows(RuntimeException.class, () -> departmentLecturerRepo.save(departmentLecturer2));
    }

}