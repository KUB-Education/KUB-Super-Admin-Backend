package education.kub.backend.ce.domain.lecturer.entity;

import education.kub.backend.ce.domain.academic_title.entity.AcademicTitleEntity;
import education.kub.backend.ce.domain.department_lecturer.entity.DepartmentLecturerEntity;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lecturers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class LecturerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    UserEntity user;

    @OneToMany(mappedBy = "lecturer", fetch = FetchType.LAZY)
    private Set<DepartmentLecturerEntity> departmentLecturers;

    @ManyToMany
    @JoinTable(
            name = "lecturer_academic_titles",
            joinColumns = @JoinColumn(name = "lecturer_id"),
            inverseJoinColumns = @JoinColumn(name = "academic_title_id")
    )
    private Set<AcademicTitleEntity> academicTitles = new HashSet<>();
}