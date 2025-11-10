package education.kub.backend.ce.domain.selected_subject_activity_group.entity;

import education.kub.backend.ce.domain.selected_subject_activity.entity.SelectedSubjectActivityEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "selected_subj_act_groups")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class SelectedSubjectActivityGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "selected_subject_activity_id", nullable = false)
    private SelectedSubjectActivityEntity selectedSubjectActivity;

    @Column(name = "name", nullable = false, length = 64)
    @NotBlank
    private String name;
}
