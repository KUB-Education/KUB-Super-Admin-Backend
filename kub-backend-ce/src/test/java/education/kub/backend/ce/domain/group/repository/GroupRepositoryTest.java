package education.kub.backend.ce.domain.group.repository;

import education.kub.backend.ce.domain.group.domain.GroupEntity;
import education.kub.backend.ce.domain.subject_ativity.domain.SubjectActivityEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupRepositoryTest {
    @Autowired
    private GroupRepository groupRepo;


    @Test
    void givenNew_whenSave_thenSuccess() {
        GroupEntity groupExpected = new GroupEntity();
        groupExpected.setName("group");
        groupExpected.setCreatedAt(Instant.now());

        assertDoesNotThrow(() -> groupRepo.save(groupExpected));

        GroupEntity groupActual = groupRepo.findById(groupExpected.getId()).get();

        assertEquals(groupExpected, groupActual);
        assertEquals(groupExpected.getName(), groupActual.getName());
        assertEquals(groupExpected.getCreatedAt(), groupActual.getCreatedAt());
    }

    @Test
    void givenNewWithBlankName_whenSave_thenException() {
        GroupEntity group = new GroupEntity();
        group.setName("         ");
        group.setCreatedAt(Instant.now());

        assertThrows(RuntimeException.class, () -> groupRepo.save(group));
    }
}