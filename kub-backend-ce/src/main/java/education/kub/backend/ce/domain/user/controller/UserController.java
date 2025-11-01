package education.kub.backend.ce.domain.user.controller;

import education.kub.backend.ce.domain.user.model.*;
import education.kub.backend.ce.domain.user.service.UserRoleService;
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

    private final UserRoleService userRoleService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<UserDetailsResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        var user = userService.createUser(userCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
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

    @PostMapping("/{id}/resend")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<UserDetailsResponse> resendUserPassword(@PathVariable Long id) {
        var user = userService.sendUserPassword(id);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<UserDetailsResponse> addUserRole(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        var user = userRoleService.addUserRoleById(userId, roleId);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<UserDetailsResponse> removeUserRole(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        var user = userRoleService.removeUserRoleById(userId, roleId);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
