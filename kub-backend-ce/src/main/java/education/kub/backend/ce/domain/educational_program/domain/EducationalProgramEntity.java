package education.kub.backend.ce.domain.educational_program.domain;

import education.kub.backend.ce.domain.specialty.domain.SpecialtyEntity;
import education.kub.backend.ce.domain.student_educational_program.domain.StudentEducationalProgramEntity;
import education.kub.backend.ce.domain.term.domain.TermEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "educational_programs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class EducationalProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "specialty_id", nullable = false)
    private SpecialtyEntity specialty;

    @Column(name = "name", nullable = false, unique = true, length=128) // not blank
    private String name;

    @Column(name = "degree_type", nullable = false, length=64)
    @Enumerated(EnumType.STRING)
    private DegreeType degreeType;

    @Column(name = "study_form", nullable = false, length=64)
    @Enumerated(EnumType.STRING)
    private StudyForm studyForm;

    @Column(name = "duration", nullable = false)
    @PositiveOrZero
    private Short duration; // in months

    @OneToMany(mappedBy = "educationalProgram", fetch = FetchType.LAZY)
    private List<TermEntity> terms;

    @OneToMany(mappedBy = "educationalProgram", fetch = FetchType.LAZY)
    private List<StudentEducationalProgramEntity> studentEducationalPrograms;


    public enum DegreeType {
        BACHELOR,
        MASTER,
        POSTGRADUATE
    }

    public enum StudyForm {
        FULL_TIME,
        PART_TIME,
        ONLINE
    }


    public EducationalProgramEntity(Long id, SpecialtyEntity specialty, String name,
                                    DegreeType degreeType, StudyForm studyForm) {
        this.id = id;
        this.specialty = specialty;
        this.name = name;
        this.degreeType = degreeType;
        this.studyForm = studyForm;
    }
}