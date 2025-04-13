package education.kub.superadmin.domain.user.service;

import education.kub.superadmin.domain.user.entity.UserEntity;
import education.kub.superadmin.domain.user.model.UserRequestDTO;
import education.kub.superadmin.domain.user.model.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    List<UserResponseDTO> getUsers();

    UserResponseDTO getUser(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    void deleteUser(Long id);

    UserResponseDTO resendUserPassword(Long id);
}
