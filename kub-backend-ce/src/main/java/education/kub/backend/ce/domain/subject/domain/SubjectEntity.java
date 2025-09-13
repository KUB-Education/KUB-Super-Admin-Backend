package education.kub.backend.ce.domain.subject.domain;

import education.kub.backend.ce.domain.subject_ativity.domain.SubjectActivityEntity;
import education.kub.backend.ce.domain.term.domain.TermEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "subjects")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "term_id", nullable = false)
    private TermEntity term;

    @Column(name = "name", nullable = false, length=128) // non-empty
    private String name;

    @Column(name = "type", nullable = false, length=32)
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<SubjectActivityEntity> subjectActivities;

    public enum Type {
        MANDATORY,
        ELECTIVE
    }


    public SubjectEntity(Long id, TermEntity term, String name, Type type) {
        this.id = id;
        this.term = term;
        this.name = name;
        this.type = type;
    }
}