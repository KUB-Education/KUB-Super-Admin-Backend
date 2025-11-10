package education.kub.backend.ce.domain.group.entity;

import education.kub.backend.ce.domain.student.entity.StudentEntity;
import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    private Instant createdAt;

    @ManyToMany
    @JoinTable(
            name = "group_students",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<StudentEntity> students = new HashSet<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<TimetableEntity> timetables;

    public void addStudent(StudentEntity student) {
        students.add(student);
        student.getGroups().add(this);
    }

    public void removeStudent(StudentEntity student) {
        students.remove(student);
        student.getGroups().remove(this);
    }

}
