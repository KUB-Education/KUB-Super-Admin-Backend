package education.kub.backend.ce.domain.role.entity;

import education.kub.backend.ce.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();

    public enum Type {
        ORGANIZER,
        SYSADMIN,
        ADMIN,
        LECTURER,
        STUDENT,
        USER
    }
}
