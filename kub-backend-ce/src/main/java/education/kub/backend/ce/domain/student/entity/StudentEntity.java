package education.kub.backend.ce.domain.student.entity;

import education.kub.backend.ce.domain.student_educational_program.domain.StudentEducationalProgramEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    UserEntity user;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<StudentEducationalProgramEntity> studentEducationalPrograms;
}