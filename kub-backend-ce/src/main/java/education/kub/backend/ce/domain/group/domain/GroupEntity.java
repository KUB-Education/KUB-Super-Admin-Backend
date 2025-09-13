package education.kub.backend.ce.domain.group.domain;

import education.kub.backend.ce.domain.academic_title.entity.AcademicTitleEntity;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.student.entity.StudentEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    @NotBlank
    private String name;

    @Column(name = "created_at", columnDefinition = "timestamptz", nullable = false)
    private Instant createdAt;

    @ManyToMany
    @JoinTable(
            name = "group_students",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<StudentEntity> students = new HashSet<>();


    public void addStudent(StudentEntity student) {
        students.add(student);
        student.getGroups().add(this);
    }

    public void removeRole(StudentEntity student) {
        students.remove(student);
        student.getGroups().remove(this);
    }

}
