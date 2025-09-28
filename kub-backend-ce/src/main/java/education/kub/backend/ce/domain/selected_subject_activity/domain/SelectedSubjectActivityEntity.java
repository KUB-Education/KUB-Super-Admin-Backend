package education.kub.backend.ce.domain.selected_subject_activity.domain;

import education.kub.backend.ce.domain.subject_ativity.domain.SubjectActivityEntity;
import education.kub.backend.ce.domain.timetable.domain.TimetableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "selected_subject_activities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class SelectedSubjectActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "timetable_id", nullable = false)
    private TimetableEntity timetable;

    @ManyToOne
    @JoinColumn(name = "subject_activity_id", nullable = false)
    private SubjectActivityEntity subjectActivity;
}
