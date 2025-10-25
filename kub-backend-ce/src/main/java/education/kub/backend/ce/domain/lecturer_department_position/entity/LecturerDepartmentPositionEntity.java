package education.kub.backend.ce.domain.lecturer_department_position.entity;

import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.position.entity.PositionEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecturer_department_positions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class LecturerDepartmentPositionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private LecturerEntity lecturer;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private PositionEntity position;

    @Column(name = "status", nullable = false, length=64)
    @Enumerated(EnumType.STRING)
    private Status status;


    public enum Status {
        ACTIVE,
        INACTIVE,
        TERMINATED
    }
}
