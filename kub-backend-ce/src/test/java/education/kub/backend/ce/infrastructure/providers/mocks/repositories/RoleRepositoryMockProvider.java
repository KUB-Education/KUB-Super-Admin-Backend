package education.kub.backend.ce.infrastructure.providers.mocks.repositories;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.role.repository.RoleRepository;
import org.mockito.Mockito;

import java.util.Optional;

public class RoleRepositoryMockProvider {
    public static RoleRepository createRoleRepositoryMock(RoleEntity role) {
        RoleRepository roleRepo = Mockito.mock(RoleRepository.class);
        Mockito.lenient().when(roleRepo.findByType(role.getType())).thenReturn(Optional.of(role));
        return roleRepo;
    }
}
