package education.kub.backend.ce.domain.room.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location", nullable = false, length = 256, unique = true) // not empty
    @NotBlank
    private String location;

    @Column(name = "capacity", nullable = false)
    @PositiveOrZero
    private Short capacity;

    @Column(name = "details", columnDefinition = "TEXT", nullable = true) // not blank
    private String details;
}