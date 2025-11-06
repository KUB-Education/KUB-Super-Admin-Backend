package education.kub.backend.ce.domain.selected_room.domain;

import education.kub.backend.ce.domain.room.domain.RoomEntity;
import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "selected_rooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class SelectedRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "timetable_id", nullable = false)
    private TimetableEntity timetable;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;
}
