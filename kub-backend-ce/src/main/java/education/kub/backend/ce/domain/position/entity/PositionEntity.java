package education.kub.backend.ce.domain.position.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "positions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class PositionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 128, unique = true) // non-blank
    @Enumerated(EnumType.STRING)
    private PositionName name;


    public enum PositionName {
        ASSISTANT,
        ASSOCIATE_PROFESSOR,
        PROFESSOR
    }
}
