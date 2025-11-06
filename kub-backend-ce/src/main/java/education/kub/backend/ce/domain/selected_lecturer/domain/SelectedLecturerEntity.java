package education.kub.backend.ce.domain.selected_lecturer.domain;

import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;
import education.kub.backend.ce.domain.timetable_class.entity.TimetableClassEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "selected_lecturers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class SelectedLecturerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "timetable_id", nullable = false)
    private TimetableEntity timetable;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private LecturerEntity lecturer;

    @ManyToMany(mappedBy = "selectedLecturers")
    private Set<TimetableClassEntity> classes = new HashSet<>();
}