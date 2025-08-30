package education.kub.backend.ce.domain.bootstrap.service;

import education.kub.backend.ce.domain.user.model.UserCreateRequest;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import education.kub.backend.ce.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BootstrapService {
    private final UserService userService;

    public UserDetailsResponse createOrganizerUser(UserCreateRequest userCreateRequest) {
        var userDetailsResponse = userService.createUser(userCreateRequest);

        userDetailsResponse = userService.addRole(userDetailsResponse.id(), "organizer");

        return userDetailsResponse;
    }
}
