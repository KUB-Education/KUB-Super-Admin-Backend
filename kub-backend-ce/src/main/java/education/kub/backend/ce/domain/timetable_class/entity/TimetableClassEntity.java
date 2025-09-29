package education.kub.backend.ce.domain.timetable_class.entity;

import education.kub.backend.ce.domain.group.domain.GroupEntity;
import education.kub.backend.ce.domain.selected_room.domain.SelectedRoomEntity;
import education.kub.backend.ce.domain.selected_subject_activity_group.domain.SelectedSubjectActivityGroupEntity;
import education.kub.backend.ce.domain.timetable.domain.TimetableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "classes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class TimetableClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "timetable_id", nullable = false)
    private TimetableEntity timetable;

    @ManyToOne
    @JoinColumn(name = "select_subj_act_group_id", nullable = false)
    private SelectedSubjectActivityGroupEntity selectedSubjectActivityGroup;

    @ManyToOne
    @JoinColumn(name = "selected_room_id", nullable = false)
    private SelectedRoomEntity selectedRoom;

    @Column(name = "type", nullable = false, length = 64)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "time_start", columnDefinition = "timestamptz", nullable = false)
    private Instant timeStart;

    @Column(name = "time_end", columnDefinition = "timestamptz", nullable = false)
    private Instant timeEnd;


    public enum Type {
        ONLINE,
        OFFLINE,
        HYBRID
    }

}
