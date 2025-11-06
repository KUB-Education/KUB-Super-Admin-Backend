package education.kub.backend.ce.infrastructure.components.user;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.role.repository.RoleRepository;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;

import education.kub.backend.ce.infrastructure.properties.user.UserProperties;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.RoleRepositoryMockProvider;
import education.kub.backend.ce.infrastructure.providers.mocks.services.TokenStoreServiceMockProvider;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.UserRepositoryMockProvider;

import education.kub.backend.ce.infrastructure.password.service.PasswordService;
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
    public UserProperties userData;
    @Autowired
    public TokenStoreService tokenStoreService;
    @Autowired
    public UserRepository userRepo;
    @Autowired
    public RoleRepository roleRepo;
    @Autowired
    public PasswordService passwordService;

    public String GetRoleIdString(RoleEntity.Type role) {
        return Long.toString(roleRepo.findByType(role).get().getId());
    }

    private String GetFirstNotDublicatedRoleIdString(UserProperties properties) {
        var user = userRepo.findById(properties.id).get();
        for (long role_id = 0; role_id < RoleEntity.Type.values().length; role_id++) {
            var role = roleRepo.findById(role_id);
            if (role.isPresent() && role.get().getUsers().contains(user)) {
                return Long.toString(role_id);
            }
        }
        return null;
    }

    public String GetFirstNotDublicatedRoleIdString() {
        return GetFirstNotDublicatedRoleIdString(userData);
    }

    private void addRole(long user_id, RoleEntity.Type roleType) {
        var user = userRepo.findById(user_id).get();
        var saved_role = roleRepo.findByType(roleType);
        var role = saved_role.orElseGet(() -> createRole(user, roleType));
        role.getUsers().add(user);
        role = roleRepo.save(role);
        user.addRole(role);
        userRepo.save(user);
    }

    public void addRole(RoleEntity.Type roleType) {
        addRole(userData.getId(), roleType);
    }

    public UserEntity initRepos() {
        roleRepo.deleteAll();
        userRepo.deleteAll();
        UserEntity user = createUser(userData);

        for (var role: RoleEntity.Type.values()) {
            if (userData.roles.contains(role)) {
                RoleEntity roleEntity = createRole(user, role);
                roleEntity = roleRepo.save(roleEntity);
                user.addRole(roleEntity);
            }
            else {
                RoleEntity roleEntity = createRole(role);
                roleRepo.save(roleEntity);
            }
        }

        userRepo.save(user);
        return user;
    }

    public UserEntity mockRepos() {
        UserEntity user = createUser(userData);
        roleRepo = RoleRepositoryMockProvider.createRoleRepositoryMock();

        for (var role: RoleEntity.Type.values()) {
            if (userData.roles.contains(role)) {
                RoleEntity roleEntity = createRole(user, role);
                roleEntity = roleRepo.save(roleEntity);
                user.addRole(roleEntity);
            }
            else {
                RoleEntity roleEntity = createRole(role);
                roleEntity = roleRepo.save(roleEntity);
            }
        }

        tokenStoreService = TokenStoreServiceMockProvider.createTokenStoreServiceMock();
        userRepo = UserRepositoryMockProvider.createUserRepositoryMock(user);

        return user;
    }

    private RoleEntity createRole(RoleEntity.Type role) {
        var roleEntity = new RoleEntity();
        roleEntity.setType(role);
        return roleEntity;
    }

    private RoleEntity createRole(UserEntity user, RoleEntity.Type role) {
        var userSet = new HashSet<UserEntity>();
        userSet.add(user);
        var roleEntity = createRole(role);
        roleEntity.setUsers(userSet);
        return roleEntity;
    }

    private UserEntity createUser(UserProperties userData) {
        UserEntity user = new UserEntity();
        user.setLastName(userData.last_name);
        user.setFirstName(userData.first_name);
        user.setMiddleName(userData.middle_name);
        user.setEmail(userData.email);
        user.setPasswordHashed(passwordService.hash(userData.password));
        user.setStatus(ACTIVATED);
        return user;
    }
}
