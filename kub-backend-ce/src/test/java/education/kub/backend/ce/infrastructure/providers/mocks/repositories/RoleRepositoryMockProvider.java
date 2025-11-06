package education.kub.backend.ce.infrastructure.providers.mocks.repositories;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.role.repository.RoleRepository;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class RoleRepositoryMockProvider {
    private static long id_count = 0;

    public static void resetMocks() {
        id_count = 0;
    }

    private static void saveRole(RoleRepository mock, RoleEntity role) {
        Mockito.lenient().doReturn(Optional.of(role)).when(mock).findById(role.getId());
        Mockito.lenient().doReturn(Optional.of(role)).when(mock).findByType(role.getType());
    }

    public static RoleRepository createRoleRepositoryMock(UserEntity user) {
        RoleRepository roleRepo = Mockito.mock(RoleRepository.class);
        Mockito.lenient().doAnswer(invocation -> {
            var saved_role = invocation.getArgument(0, RoleEntity.class);
            saved_role.setId(id_count++);
            saveRole(roleRepo, saved_role);
            return saved_role;
        }).when(roleRepo).save(any(RoleEntity.class));
        return roleRepo;
    }

    public static RoleRepository createRoleRepositoryMock() {
        RoleRepository roleRepo = Mockito.mock(RoleRepository.class);
        Mockito.lenient().doAnswer(invocation -> {
            var saved_role = invocation.getArgument(0, RoleEntity.class);
            saved_role.setId(id_count++);
            saveRole(roleRepo, saved_role);
            return saved_role;
        }).when(roleRepo).save(any(RoleEntity.class));
        return roleRepo;
    }
}
