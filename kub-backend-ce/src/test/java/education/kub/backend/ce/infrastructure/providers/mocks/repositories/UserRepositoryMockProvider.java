package education.kub.backend.ce.infrastructure.providers.mocks.repositories;


import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import org.mockito.Mockito;

import java.util.Optional;

public class UserRepositoryMockProvider {

    public static UserRepository createUserRepositoryMock(UserEntity user) {
        Long id = user.getId();
        String email = user.getEmail();
        UserRepository userRepo = Mockito.mock(UserRepository.class);
        Mockito.lenient().when(userRepo.save(user)).thenReturn(user);
        Mockito.lenient().when(userRepo.findById(id)).thenReturn(Optional.of(user));
        Mockito.lenient().when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.lenient().when(userRepo.findWithRolesById(id)).thenReturn(Optional.of(user));
        Mockito.lenient().when(userRepo.existsByEmailAndDeletedAtIsNull(email)).thenReturn(false);
        Mockito.lenient().when(userRepo.findWithRolesByEmailAndDeletedAtIsNull(email)).thenReturn(Optional.of(user));
        return userRepo;
    }
}
