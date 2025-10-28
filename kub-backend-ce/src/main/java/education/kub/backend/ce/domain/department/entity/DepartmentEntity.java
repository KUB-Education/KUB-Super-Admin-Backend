package education.kub.backend.ce.domain.department.entity;

import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "departments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length=128) // non-empty, non-blank
    private String name;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<LecturerDepartmentPositionEntity> lecturerDepartmentPositions;
}