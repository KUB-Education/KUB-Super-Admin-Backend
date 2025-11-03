package education.kub.backend.ce.domain.room.repository;

import education.kub.backend.ce.domain.room.domain.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    Boolean existsByLocation(String location);
    Boolean existsByLocationAndIdNot(String location, Long id);
    List<RoomEntity> findByLocationContainingAndCapacityGreaterThanEqual(String location, Short capacity);
    List<RoomEntity> findByLocationContaining(String location);
    List<RoomEntity> findByCapacityGreaterThanEqual(Short capacity);
}
