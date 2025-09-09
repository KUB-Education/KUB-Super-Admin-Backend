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

    @Column(name = "name", nullable = false, unique = true, length = 128) // not blank
    private String name;

    /* commented, because this property don't automatically adjusts with opposite side of relation (https://stackoverflow.com/a/30474303)
    @ManyToMany(mappedBy = "academicTitles", fetch = FetchType.LAZY)
    private Set<LecturerEntity> lecturers = new HashSet<>();*/
}
