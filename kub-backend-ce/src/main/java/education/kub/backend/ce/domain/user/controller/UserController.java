package education.kub.backend.ce.domain.user.controller;

import education.kub.backend.ce.domain.user.model.*;
import education.kub.backend.ce.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<UserDetailsResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        var user = userService.createUser(userCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<List<UserDetailsResponse>> getAllUsers() {
        var users = userService.getUsers();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable Long id) {
        var user = userService.getUserWithRoles(id);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<UserDetailsResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        var user = userService.updateUser(id, userUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/{id}/add-role")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<UserDetailsResponse> addUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UserAddRoleRequest userAddRoleRequest
    ) {
        var user = userService.addUserRole(id, userAddRoleRequest.type());

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/{id}/remove-role")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<UserDetailsResponse> removeUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UserRemoveRoleRequest userRemoveRoleRequest
    ) {
        var user = userService.removeUserRole(id, userRemoveRoleRequest.type());

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
