package education.kub.superadmin.entities;

import education.kub.superadmin.dto.AdminResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    UserEntity user;

    public AdminResponseDTO toAdminResponseDto() {
        return AdminResponseDTO.builder()
                .id(this.id)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .status(user.getStatus())
                .build();
    }
}