package education.kub.backend.ce.domain.department_lecturer.entity;

import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "department_lecturers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class DepartmentLecturerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private LecturerEntity lecturer;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    @Column(name = "position", nullable = false, length=64)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(name = "status", nullable = false, length=64)
    @Enumerated(EnumType.STRING)
    private Status status;


    public enum Position{
        ASSISTANT,
        ASSOCIATE_PROFESSOR,
        PROFESSOR
    }

    public enum Status {
        ACTIVE,
        INACTIVE,
        TERMINATED
    }
}
