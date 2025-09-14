package education.kub.backend.ce.integration;

import education.kub.backend.ce.domain.group.domain.GroupEntity;
import education.kub.backend.ce.domain.group.repository.GroupRepository;
import education.kub.backend.ce.domain.student.entity.StudentEntity;
import education.kub.backend.ce.domain.student.repository.StudentRepository;
import education.kub.backend.ce.domain.subject_ativity.domain.SubjectActivityEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GroupStudentsTest {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private GroupRepository groupRepo;

    private UserEntity user1;
    private UserEntity user2;
    private StudentEntity student;
    private GroupEntity group;

    @BeforeEach
    void setUp(){
        user1 = new UserEntity();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setMiddleName("Smith");
        user1.setEmail("someemail1@someemail.com");
        userRepo.save(user1);

        user2 = new UserEntity();
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setMiddleName("Smith");
        user2.setEmail("someemail2@someemail.com");
        userRepo.save(user2);
    }

    @AfterEach
    void tearDown() {
        userRepo.delete(user1);
        userRepo.delete(user2);
    }

    @Test
    void givenNew_whenSave_thenSuccess() {
        // insert data
        GroupEntity groupExpected = new GroupEntity();
        groupExpected.setName("Group");
        groupExpected.setCreatedAt(Instant.now());
        groupRepo.save(groupExpected);

        StudentEntity student1 = new StudentEntity();
        student1.setUser(user1);
        studentRepo.save(student1);

        StudentEntity student2 = new StudentEntity();
        student2.setUser(user2);
        studentRepo.save(student2);


        groupExpected.addStudent(student1);
        groupExpected.addStudent(student2);
        groupRepo.save(groupExpected);
        studentRepo.save(student1);
        studentRepo.save(student2);


        // check data
        GroupEntity groupActual = groupRepo.findById(groupExpected.getId()).get();
        StudentEntity student1Actual = studentRepo.findById(student1.getId()).get();
        StudentEntity student2Actual = studentRepo.findById(student2.getId()).get();


        assertEquals(groupExpected.getStudents(), groupActual.getStudents());
        assertEquals(student1.getGroups(), student1Actual.getGroups());
        assertEquals(student2.getGroups(), student2Actual.getGroups());
    }

}
