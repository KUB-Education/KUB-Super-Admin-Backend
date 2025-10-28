package education.kub.backend.ce.infrastructure.components.user;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.role.repository.RoleRepository;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;

import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.user.UserProperties;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.RoleRepositoryMockProvider;
import education.kub.backend.ce.infrastructure.providers.mocks.services.TokenStoreServiceMockProvider;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.UserRepositoryMockProvider;

import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.infrastructure.token.provider.JwtTokenProvider;
import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;

import static education.kub.backend.ce.domain.user.entity.UserEntity.Status.ACTIVATED;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Setter
@Getter
@Component
public class UserComponent {
    public LoginProperties loginData;
    @Autowired
    public UserProperties userData;

    @Autowired
    public TokenStoreService tokenStoreService;
    @Autowired
    public UserRepository userRepo;
    @Autowired
    public RoleRepository roleRepo;
    @Autowired
    public PasswordService passwordService;
    @Autowired
    public JwtTokenProvider jwtTokenProvider;

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
        user.setId(userData.id);
        role.setId(userData.roles.id);
        injectRole(user, role);
        tokenStoreService = TokenStoreServiceMockProvider.createTokenStoreServiceMock();
        userRepo = UserRepositoryMockProvider.createUserRepositoryMock(user);
        roleRepo = RoleRepositoryMockProvider.createRoleRepositoryMock(role);
    }

    private RoleEntity createRole(UserEntity user) {
        var userSet = new HashSet<UserEntity>();
        userSet.add(user);
        var roleEntity = new RoleEntity();
        roleEntity.setType(userData.roles.role);
        roleEntity.setUsers(userSet);
        return roleEntity;
    }

    private UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setLastName(userData.last_name);
        user.setFirstName(userData.first_name);
        user.setMiddleName(userData.middle_name);
        user.setEmail(loginData.email);
        user.setPasswordHashed(passwordService.hash(loginData.password));
        user.setStatus(ACTIVATED);
        return user;
    }

    private void injectRole(UserEntity user, RoleEntity role) {
        var roles = new HashSet<RoleEntity>();
        roles.add(role);
        user.setRoles(roles);
    }
}
