package education.kub.backend.ce.domain.selected_room.repository;

import education.kub.backend.ce.domain.selected_room.domain.SelectedRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedRoomRepository extends JpaRepository<SelectedRoomEntity, Long> {
}
