package education.kub.backend.ce.domain.subject_activity.domain;

import education.kub.backend.ce.domain.subject.domain.SubjectEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;


@Entity
@Table(name = "subject_activities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class SubjectActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectEntity subject;

    @Column(name = "type", nullable = false, length=32)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "academic_hours", nullable = false)
    @PositiveOrZero
    private Short academicHours;


    public enum Type {
        LECTURE,
        LABORATORY,
        SEMINAR,
        EXAM,
        CREDIT,
        RESUBMISSION,
        COMMISSION,
        CONSULTATION
    }
}