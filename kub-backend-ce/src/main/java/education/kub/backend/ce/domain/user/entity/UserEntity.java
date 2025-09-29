package education.kub.backend.ce.domain.user.entity;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name", nullable = false, length = 32)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 32)
    private String firstName;

    @Column(name = "middle_name", length = 32)
    private String middleName;

    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @Column(name = "status", length = 32)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "password_hashed", length = 64)
    private String passwordHashed; // salted hash

    @Column(name = "temporary_password_hashed", length = 64)
    private String temporaryPasswordHashed; // salted hash

    @Column(name = "temporary_password_expires_at", columnDefinition = "timestamptz")
    private Instant temporaryPasswordExpiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "timestamptz")
    private Instant deletedAt;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    public void addRole(RoleEntity role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(RoleEntity role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public enum Status {
        EMAIL_SENDING_FAILURE,
        ACTIVATION_PENDING,
        ACTIVATION_EXPIRED,
        ACTIVATED,
        RECOVERY_PENDING
    }
}