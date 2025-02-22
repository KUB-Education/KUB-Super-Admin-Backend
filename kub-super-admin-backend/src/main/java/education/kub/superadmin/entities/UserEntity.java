package education.kub.superadmin.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue
    @ColumnDefault("gen_random_uuid()")
    private UUID id;

    @Column(name = "last_name", nullable = false, length=32)
    private String lastName;

    @Column(name = "first_name", nullable = false, length=32)
    private String firstName;

    @Column(name = "middle_name", nullable = true, length=32)
    private String middleName;

    @Column(name = "email", unique = true, nullable = false, length=64)
    private String email;

    @Column(name = "status", nullable = false, length=32)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "password_hashed", nullable = true, length=64)
    private String passwordHashed; // salted hash

    @Column(name = "temporary_password_hashed", nullable = true, length=64)
    private String temporaryPasswordHashed; // salted hash

    @Column(name = "temporary_password_expiration", nullable = true)
    private LocalDateTime temporaryPasswordExpiration;



    public enum Status{
        EMAIL_SENDING_FAILURE,
        ACTIVATION_PENDING,
        ACTIVATION_EXPIRED,
        ACTIVATED,
        RECOVERY_PENDING
    }
}