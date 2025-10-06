package education.kub.backend.ce.domain.department.repository;

import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DepartmentRepositoryTest {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @Rollback(false)
    void givenNewDepartment_whenSave_thenSuccess() {
        DepartmentEntity d = new DepartmentEntity();
        d.setName("New Department");
        departmentRepository.save(d);

        assertThat(departmentRepository.findById(d.getId()).orElse(null)).isEqualTo(d);
        assertThat(departmentRepository.findById(d.getId()).orElse(null).getName()).isEqualTo(d.getName());
    }

    @Test
    void givenNewDepartmentWithNullName_whenSave_thenException() {
        DepartmentEntity d = new DepartmentEntity();

        assertThrows(RuntimeException.class, () -> departmentRepository.save(d));
    }

    @Test
    void givenNewDepartmentWithEmptyName_whenSave_thenException() {
        DepartmentEntity d = new DepartmentEntity();
        d.setName("");

        assertThrows(RuntimeException.class, () -> departmentRepository.save(d));
    }

    @Test
    void givenNewDepartmentWithBlankName_whenSave_thenException() {
        DepartmentEntity d = new DepartmentEntity();
        d.setName("         ");

        assertThrows(RuntimeException.class, () -> departmentRepository.save(d));
    }

}