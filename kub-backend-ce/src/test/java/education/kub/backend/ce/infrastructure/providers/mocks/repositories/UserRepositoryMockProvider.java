package education.kub.backend.ce.infrastructure.providers.mocks.repositories;


import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

public class UserRepositoryMockProvider {
    private static long id_count = 0;
    private static final Set<String> emails = new HashSet<>();

    private static void saveUser(UserRepository mock, UserEntity user) {
        Mockito.lenient().doReturn(Optional.of(user)).when(mock).findById(user.getId());
        Mockito.lenient().doReturn(Optional.of(user)).when(mock).findByIdAndDeletedAtIsNull(user.getId());
        Mockito.lenient().doReturn(Optional.of(user)).when(mock).findByEmail(user.getEmail());
        Mockito.lenient().doReturn(Optional.of(user)).when(mock).findWithRolesById(user.getId());
        Mockito.lenient().doReturn(Optional.of(user)).when(mock).findWithRolesByEmailAndDeletedAtIsNull(user.getEmail());
    }

    public static void resetMocks() {
        id_count = 0;
        emails.clear();
    }

    public static UserRepository createUserRepositoryMock(UserEntity user) {
        Long id = user.getId();
        String email = user.getEmail();
        UserRepository userRepo = Mockito.mock(UserRepository.class);
        Mockito.lenient().doAnswer(invocation -> {
            var saved_user = invocation.getArgument(0, UserEntity.class);
            var user_id = saved_user.getId();
            if (user_id == null || !(user_id >= 0 && user_id < id_count)) {
                saved_user.setId(id_count++);
            }
            saveUser(userRepo, saved_user);
            emails.add(saved_user.getEmail());
            return saved_user;
        }).when(userRepo).save(any(UserEntity.class));
        Mockito.lenient().
                doAnswer(invocation ->  {
                    return emails.contains(invocation.getArgument(0, String.class));
                }).when(userRepo).existsByEmailAndDeletedAtIsNull(any(String.class));
        userRepo.save(user);
        return userRepo;
    }
}
