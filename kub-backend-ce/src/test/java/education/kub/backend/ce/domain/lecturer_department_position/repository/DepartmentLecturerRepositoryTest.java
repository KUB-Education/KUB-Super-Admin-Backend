package education.kub.backend.ce.domain.lecturer_department_position.repository;

import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.repository.DepartmentRepository;
import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;
import education.kub.backend.ce.domain.position.entity.PositionEntity;
import education.kub.backend.ce.domain.position.repository.PositionRepository;
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
    private PositionRepository positionRepo;

    @Autowired
    private LecturerDepartmentPositionRepository lecturerDepartmentPositionRepo;

    private UserEntity user;
    private LecturerEntity lecturer;
    private DepartmentEntity department;
    private PositionEntity position;

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

        position = positionRepo.findAll().get(0);
    }

    @AfterEach
    void tearDown() {
        lecturerRepo.delete(lecturer);
        userRepo.delete(user);
        departmentRepo.delete(department);
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        var lecDepPosExpected = new LecturerDepartmentPositionEntity();
        lecDepPosExpected.setLecturer(lecturer);
        lecDepPosExpected.setDepartment(department);
        lecDepPosExpected.setPosition(position);
        lecDepPosExpected.setStatus(LecturerDepartmentPositionEntity.Status.ACTIVE);

        assertDoesNotThrow(() -> lecturerDepartmentPositionRepo.save(lecDepPosExpected));


        var lecDepPosActual = lecturerDepartmentPositionRepo.findById(lecDepPosExpected.getId()).get();

        assertEquals(lecDepPosExpected, lecDepPosActual);
        assertEquals(lecDepPosExpected.getLecturer(), lecDepPosActual.getLecturer());
        assertEquals(lecDepPosExpected.getDepartment(), lecDepPosActual.getDepartment());
        assertEquals(lecDepPosExpected.getPosition(), lecDepPosActual.getPosition());
        assertEquals(lecDepPosExpected.getStatus(), lecDepPosActual.getStatus());
    }

    // unique(lecturer_id, department_id) constraint is violated
    @Test
    void givenTwoNewWithSameLecturerAndDepartment_whenSave_thenException() {
        var lecDepPos1 = new LecturerDepartmentPositionEntity();
        lecDepPos1.setLecturer(lecturer);
        lecDepPos1.setDepartment(department);
        lecDepPos1.setPosition(position);
        lecDepPos1.setStatus(LecturerDepartmentPositionEntity.Status.ACTIVE);

        var lecDepPos2 = new LecturerDepartmentPositionEntity();
        lecDepPos2.setLecturer(lecturer);
        lecDepPos2.setDepartment(department);
        lecDepPos2.setPosition(position);
        lecDepPos2.setStatus(LecturerDepartmentPositionEntity.Status.INACTIVE);

        assertDoesNotThrow(() -> lecturerDepartmentPositionRepo.save(lecDepPos1));
        assertThrows(RuntimeException.class, () -> lecturerDepartmentPositionRepo.save(lecDepPos2));
    }

}