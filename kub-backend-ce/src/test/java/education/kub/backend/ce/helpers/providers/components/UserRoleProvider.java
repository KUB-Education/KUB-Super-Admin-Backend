package education.kub.backend.ce.helpers.providers.components;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.role.repository.RoleRepository;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.helpers.providers.request_wrappers.auth.LoginProvider;
import education.kub.backend.ce.helpers.providers.mocks.repositories.RoleRepositoryMockProvider;
import education.kub.backend.ce.helpers.providers.mocks.services.TokenStoreServiceMockProvider;
import education.kub.backend.ce.helpers.providers.mocks.repositories.UserRepositoryMockProvider;
import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.infrastructure.token.provider.JwtTokenProvider;
import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;

import static education.kub.backend.ce.domain.user.entity.UserEntity.Status.ACTIVATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserRoleProvider extends LoginProvider {

    protected final long user_id = 0;
    protected final long role_id = 0;


    protected final String last_name = "Doe";
    protected final String first_name = "John";
    protected final String middle_name = "Edward";

    RoleEntity.Type type = RoleEntity.Type.USER;

    @Autowired
    protected TokenStoreService tokenStoreService;
    @Autowired
    protected UserRepository userRepo;
    @Autowired
    protected RoleRepository roleRepo;
    @Autowired
    protected PasswordService passwordService;
    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    public void initRepos() {
        roleRepo.deleteAll();
        userRepo.deleteAll();
        UserEntity user = createUser();
        RoleEntity role = createRole(user);
        injectRole(user, role);
        roleRepo.save(role);
        userRepo.save(user);
    }

    public void mockRepos() {
        UserEntity user = createUser();
        RoleEntity role = createRole(user);
        user.setId(user_id);
        role.setId(role_id);
        injectRole(user, role);
        tokenStoreService = TokenStoreServiceMockProvider.createTokenStoreServiceMock();
        userRepo = UserRepositoryMockProvider.createUserRepositoryMock(user);
        roleRepo = RoleRepositoryMockProvider.createRoleRepositoryMock(role);
    }

    private RoleEntity createRole(UserEntity user) {
        var userSet = new HashSet<UserEntity>();
        userSet.add(user);
        var roleEntity = new RoleEntity();
        roleEntity.setType(type);
        roleEntity.setUsers(userSet);
        return roleEntity;
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setLastName(last_name);
        user.setFirstName(first_name);
        user.setMiddleName(middle_name);
        user.setEmail(email);
        user.setPasswordHashed(passwordService.hash(password));
        user.setStatus(ACTIVATED);
        return user;
    }

    private void injectRole(UserEntity user, RoleEntity role) {
        var roles = new HashSet<RoleEntity>();
        roles.add(role);
        user.setRoles(roles);
    }
}
