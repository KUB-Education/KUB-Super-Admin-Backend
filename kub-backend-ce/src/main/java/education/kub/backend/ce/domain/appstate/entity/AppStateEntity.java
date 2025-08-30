package education.kub.backend.ce.domain.appstate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "app_states")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppStateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key", nullable = false, length = 32)
    private String key;

    @Column(name = "value", nullable = false, length = 32)
    private String value;
}
