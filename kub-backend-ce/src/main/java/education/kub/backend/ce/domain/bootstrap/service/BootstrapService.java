package education.kub.backend.ce.domain.bootstrap.service;

import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.user.model.UserCreateRequest;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import education.kub.backend.ce.domain.user.service.UserRoleService;
import education.kub.backend.ce.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BootstrapService {
    private final UserService userService;

    private final UserRoleService userRoleService;

    public UserDetailsResponse createOrganizerUser(UserCreateRequest userCreateRequest) {
        var userDetailsResponse = userService.createUser(userCreateRequest);

        userDetailsResponse = userRoleService.addUserRoleByType(userDetailsResponse.id(), RoleEntity.Type.ORGANIZER);

        return userDetailsResponse;
    }
}
