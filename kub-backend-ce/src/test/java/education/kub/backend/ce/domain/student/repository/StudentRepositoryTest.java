package education.kub.backend.ce.domain.student.repository;

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
class StudentRepositoryTest {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StudentRepository studentRepo;


    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMiddleName("Smith");
        user.setEmail("someemail");
        userRepo.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepo.delete(user);
    }


    @Test
    void givenNewStudent_whenSave_thenSuccess() {
        StudentEntity s = new StudentEntity();
        s.setUser(user);
        studentRepo.save(s);

        assertThat(studentRepo.findById(s.getId()).orElse(null)).isEqualTo(s);
    }

    @Test
    void givenTwoStudentsForSameUser_whenSave_thenException() {
        StudentEntity s1 = new StudentEntity();
        StudentEntity s2 = new StudentEntity();
        s1.setUser(user);
        s2.setUser(user);

        assertDoesNotThrow(() -> studentRepo.save(s1));
        assertThrows(RuntimeException.class, () -> studentRepo.save(s2));
    }


}