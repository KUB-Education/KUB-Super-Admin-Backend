package education.kub.backend.ce.domain.academic_title.entity;

import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "academic_titles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class AcademicTitleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 128)
    @Enumerated(EnumType.STRING)
    private AcademicTitleName name;

    /* commented, because this property don't automatically adjusts with opposite side of relation (https://stackoverflow.com/a/30474303)
    @ManyToMany(mappedBy = "academicTitles", fetch = FetchType.LAZY)
    private Set<LecturerEntity> lecturers = new HashSet<>();*/

    public enum AcademicTitleName {
        // candidates
        CANDIDATE_OF_ARCHITECTURE,
        CANDIDATE_OF_BIOLOGICAL_SCIENCES,
        CANDIDATE_OF_VETERINARY_SCIENCES,
        CANDIDATE_OF_MILITARY_SCIENCES,
        CANDIDATE_OF_GEOGRAPHICAL_SCIENCES,
        CANDIDATE_OF_GEOLOGICAL_SCIENCES,
        CANDIDATE_OF_SCIENCES_IN_PUBLIC_ADMINISTRATION,
        CANDIDATE_OF_ECONOMIC_SCIENCES,
        CANDIDATE_OF_HISTORICAL_SCIENCES,
        CANDIDATE_OF_CULTUROLOGY,
        CANDIDATE_OF_MEDICAL_SCIENCES,
        CANDIDATE_OF_ARTS,
        CANDIDATE_OF_PEDAGOGICAL_SCIENCES,
        CANDIDATE_OF_POLITICAL_SCIENCES,
        CANDIDATE_OF_PSYCHOLOGICAL_SCIENCES,
        CANDIDATE_OF_AGRICULTURAL_SCIENCES,
        CANDIDATE_OF_SCIENCES_IN_SOCIAL_COMMUNICATIONS,
        CANDIDATE_OF_SOCIOLOGICAL_SCIENCES,
        CANDIDATE_OF_TECHNICAL_SCIENCES,
        CANDIDATE_OF_PHYSICAL_AND_MATHEMATICAL_SCIENCES,
        CANDIDATE_OF_PHARMACEUTICAL_SCIENCES,
        CANDIDATE_OF_SCIENCES_IN_PHYSICAL_EDUCATION_AND_SPORT,
        CANDIDATE_OF_PHILOLOGICAL_SCIENCES,
        CANDIDATE_OF_PHILOSOPHY,
        CANDIDATE_OF_CHEMICAL_SCIENCES,
        CANDIDATE_OF_LAW,

        // doctors
        DOCTOR_OF_ARCHITECTURE,
        DOCTOR_OF_BIOLOGICAL_SCIENCES,
        DOCTOR_OF_VETERINARY_SCIENCES,
        DOCTOR_OF_MILITARY_SCIENCES,
        DOCTOR_OF_GEOGRAPHICAL_SCIENCES,
        DOCTOR_OF_GEOLOGICAL_SCIENCES,
        DOCTOR_OF_SCIENCES_IN_PUBLIC_ADMINISTRATION,
        DOCTOR_OF_ECONOMIC_SCIENCES,
        DOCTOR_OF_HISTORICAL_SCIENCES,
        DOCTOR_OF_CULTUROLOGY,
        DOCTOR_OF_MEDICAL_SCIENCES,
        DOCTOR_OF_ARTS,
        DOCTOR_OF_PEDAGOGICAL_SCIENCES,
        DOCTOR_OF_POLITICAL_SCIENCES,
        DOCTOR_OF_PSYCHOLOGICAL_SCIENCES,
        DOCTOR_OF_AGRICULTURAL_SCIENCES,
        DOCTOR_OF_SCIENCES_IN_SOCIAL_COMMUNICATIONS,
        DOCTOR_OF_SOCIOLOGICAL_SCIENCES,
        DOCTOR_OF_TECHNICAL_SCIENCES,
        DOCTOR_OF_PHYSICAL_AND_MATHEMATICAL_SCIENCES,
        DOCTOR_OF_PHARMACEUTICAL_SCIENCES,
        DOCTOR_OF_SCIENCES_IN_PHYSICAL_EDUCATION_AND_SPORT,
        DOCTOR_OF_PHILOLOGICAL_SCIENCES,
        DOCTOR_OF_PHILOSOPHY,
        DOCTOR_OF_CHEMICAL_SCIENCES,
        DOCTOR_OF_LAW,

        // PhD (not full)
        PHD_OF_MATHEMATICS,
        PHD_OF_STATISTICS,
        PHD_OF_APPLIED_MATH,
        PHD_OF_SOFTWARE_ENGINEERING,
        PHD_OF_COMPUTER_SCIENCES,
        PHD_OF_SYSTEM_ANALYSIS,
        PHD_OF_CYBERSECURITY,
        PHD_OF_INFORMATION_SYSTEMS_AND_TECHNOLOGIES,
        PHD_OF_COMPUTER_ENGINEERING
    }
}
