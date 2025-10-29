package education.kub.backend.ce.domain.specialty.entity;

import education.kub.backend.ce.domain.educational_program.domain.EducationalProgramEntity;
import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "specialties")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class SpecialtyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length=32)
    @NotEmpty
    private String code;

    @Column(name = "name", nullable = false, length=128)
    @NotEmpty
    private String name;

    @ManyToOne
    @JoinColumn(name = "study_field_id", nullable = false)
    private StudyFieldEntity studyField;

    @OneToMany(mappedBy = "specialty", fetch = FetchType.LAZY)
    private List<EducationalProgramEntity> educationalPrograms = new ArrayList<>();

    public SpecialtyEntity(Long id, String code, String name, StudyFieldEntity studyField){
        this.id = id;
        this.code = code;
        this.name = name;
        this.studyField = studyField;
    }
}