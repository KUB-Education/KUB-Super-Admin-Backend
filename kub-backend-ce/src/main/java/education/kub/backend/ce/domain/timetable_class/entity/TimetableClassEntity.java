package education.kub.backend.ce.domain.timetable_class.entity;

import education.kub.backend.ce.domain.selected_lecturer.domain.SelectedLecturerEntity;
import education.kub.backend.ce.domain.selected_room.domain.SelectedRoomEntity;
import education.kub.backend.ce.domain.selected_subject_activity_group.domain.SelectedSubjectActivityGroupEntity;
import education.kub.backend.ce.domain.timetable.domain.TimetableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "class_selected_lecturers",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "selected_lecturer_id")
    )
    private Set<SelectedLecturerEntity> selectedLecturers = new HashSet<>();


    public void addSelectedLecturer(SelectedLecturerEntity selectedLecturer) {
        selectedLecturers.add(selectedLecturer);
        selectedLecturer.getClasses().add(this);
    }

    public void removeSelectedLecturer(SelectedLecturerEntity selectedLecturer) {
        selectedLecturers.remove(selectedLecturer);
        selectedLecturer.getClasses().remove(this);
    }



    public enum Type {
        ONLINE,
        OFFLINE,
        HYBRID
    }

}
