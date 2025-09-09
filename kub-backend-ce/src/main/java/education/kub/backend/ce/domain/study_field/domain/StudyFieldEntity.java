package education.kub.backend.ce.domain.study_field.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "study_fields")
@NoArgsConstructor
////////////////////////////////////////@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class StudyFieldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length=32)
    @NotEmpty
    private String code;

    @Column(name = "name", nullable = false, length=128)
    @NotEmpty
    private String name;

/*    @OneToMany(mappedBy = "studyField", fetch = FetchType.LAZY)
    private List<SpecialtyEntity> specialties;*/

    public StudyFieldEntity(Long id, String code, String name){
        this.id = id;
        this.code = code;
        this.name = name;
    }
}