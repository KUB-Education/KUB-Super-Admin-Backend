package education.kub.backend.ce.domain.room.repository;

import education.kub.backend.ce.domain.room.domain.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
}
