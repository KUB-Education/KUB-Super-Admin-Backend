package education.kub.superadmin.domain.admin.entity;

import education.kub.superadmin.domain.admin.model.AdminResponseDTO;
import education.kub.superadmin.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "admin")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    UserEntity user;

    public AdminResponseDTO toAdminResponseDto() {
        return new AdminResponseDTO(
                this.id,
                user.getLastName(),
                user.getFirstName(),
                user.getMiddleName() == null ? "" : user.getMiddleName(),
                user.getEmail(),
                user.getStatus()
        );
    }
}