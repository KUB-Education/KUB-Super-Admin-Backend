package education.kub.backend.ce.domain.student_educational_program.domain;


import education.kub.backend.ce.domain.educational_program.domain.EducationalProgramEntity;
import education.kub.backend.ce.domain.student.entity.StudentEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "student_educational_programs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class StudentEducationalProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "educational_program_id", nullable = false)
    private EducationalProgramEntity educationalProgram;

    @Column(name = "start_date", columnDefinition = "timestamptz", nullable = false)
    private Instant startDate;

    @Column(name = "finish_date", columnDefinition = "timestamptz", nullable = false)
    private Instant finishDate;

    @Column(name = "tuition", nullable = false, length=64)
    @Enumerated(EnumType.STRING)
    private Tuition tuition;

    @Column(name = "status", nullable = false, length=64)
    @Enumerated(EnumType.STRING)
    private Status status;


    public enum Tuition {
        BUDGET,
        CONTRACT
    }

    public enum Status {
        ACTIVE,
        INACTIVE,
        TERMINATED,
        GRADUATED
    }

}