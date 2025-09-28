package education.kub.backend.ce.domain.timetable.domain;

import education.kub.backend.ce.domain.group.domain.GroupEntity;
import education.kub.backend.ce.domain.selected_lecturer.domain.SelectedLecturerEntity;
import education.kub.backend.ce.domain.specialty.domain.SpecialtyEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timetables")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class TimetableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @Column(name = "time_start", columnDefinition = "timestamptz")
    private Instant timeStart;

    @Column(name = "time_end", columnDefinition = "timestamptz")
    private Instant timeEnd;

    @Column(name = "status", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "timetable", fetch = FetchType.LAZY)
    private List<SelectedLecturerEntity> selectedLecturers = new ArrayList<>();
    

    public enum Status{
        DRAFT, // just draft, no collisions.
        STAGED, // collisions checks enabled
        PUBLISHED, // users can see, + checks.
        ARCHIVED
    }
}
