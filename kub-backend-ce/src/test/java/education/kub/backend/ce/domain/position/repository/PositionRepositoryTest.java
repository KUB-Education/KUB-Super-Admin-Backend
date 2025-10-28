package education.kub.backend.ce.domain.position.repository;

import education.kub.backend.ce.domain.position.entity.PositionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PositionRepositoryTest {
    @Autowired
    private PositionRepository positionRepo;

    @Test
    void checkPredefinedPositionsExist() {
        for(var positionName : PositionEntity.PositionName.values()){
            assertTrue(positionRepo.existsByName(positionName));
        }
    }

    @Test
    void givenNewPositionWithExistingName_whenSave_thenException() {
        PositionEntity position = new PositionEntity();
        position.setName(PositionEntity.PositionName.PROFESSOR);

        // violates unique constraint for name, because this name is already inserted
        assertThrows(RuntimeException.class, () -> positionRepo.save(position));
    }

    @Test
    void givenNewPositionWithNullName_whenSave_thenException() {
        PositionEntity position = new PositionEntity();

        assertThrows(RuntimeException.class, () -> positionRepo.save(position));
    }
}