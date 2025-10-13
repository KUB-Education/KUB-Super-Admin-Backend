package education.kub.backend.ce.helpers.users;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.helpers.auth.LoginProvider;
import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.infrastructure.token.provider.JwtTokenProvider;
import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

import static education.kub.backend.ce.domain.user.entity.UserEntity.Status.ACTIVATED;

public class UserProvider extends LoginProvider {

    protected final long id = 0;

    protected final String last_name = "Doe";
    protected final String first_name = "John";
    protected final String middle_name = "Edward";

    @Autowired
    protected TokenStoreService tokenStoreService;
    @Autowired
    protected UserRepository userRepo;
    @Autowired
    protected PasswordService passwordService;
    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    protected void CreateUser() {
        userRepo.deleteAll();
        UserEntity user = new UserEntity();
        user.setLastName(last_name);
        user.setFirstName(first_name);
        user.setMiddleName(middle_name);
        user.setEmail(email);
        user.setPasswordHashed(passwordService.hash(password));
        user.setStatus(ACTIVATED);
        user.setTemporaryPasswordHashed(null);
        user.setTemporaryPasswordExpiresAt(null);
        user.setCreatedAt(null);
        user.setUpdatedAt(null);
        user.setDeletedAt(null);
        var userSet = new HashSet<UserEntity>();
        userSet.add(user);
        RoleEntity role = new RoleEntity(id, RoleEntity.Type.ADMIN, userSet);
        var roles = new HashSet<RoleEntity>();
        roles.add(role);
        userRepo.save(user);
    }
}
