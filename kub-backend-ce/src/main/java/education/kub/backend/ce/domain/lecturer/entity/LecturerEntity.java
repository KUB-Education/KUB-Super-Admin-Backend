package education.kub.backend.ce.domain.lecturer.entity;

import education.kub.backend.ce.domain.academic_title.entity.AcademicTitleEntity;
import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "lecturer", fetch = FetchType.LAZY)
    private Set<LecturerDepartmentPositionEntity> lecturerDepartmentPositions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "lecturer_academic_titles",
            joinColumns = @JoinColumn(name = "lecturer_id"),
            inverseJoinColumns = @JoinColumn(name = "academic_title_id")
    )
    private Set<AcademicTitleEntity> academicTitles = new HashSet<>();

    public boolean hasAcademicTitle(AcademicTitleEntity academicTitle) {
        return academicTitles.contains(academicTitle);
    }

    public void addAcademicTitle(AcademicTitleEntity academicTitle) {
        academicTitles.add(academicTitle);
    }

    public void removeAcademicTitle(AcademicTitleEntity academicTitle) {
        academicTitles.remove(academicTitle);
    }
}