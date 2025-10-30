package education.kub.backend.ce.domain.term.domain;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import education.kub.backend.ce.domain.subject.domain.SubjectEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "terms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class TermEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "educational_program_id", nullable = false)
    private EducationalProgramEntity educationalProgram;

    @Column(name = "number", nullable = false)
    @PositiveOrZero
    private Short number;

    @OneToMany(mappedBy = "term", fetch = FetchType.LAZY)
    private List<SubjectEntity> subjects;
}