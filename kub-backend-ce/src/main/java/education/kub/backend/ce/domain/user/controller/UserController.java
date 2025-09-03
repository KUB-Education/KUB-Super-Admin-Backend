package education.kub.backend.ce.domain.user.controller;

import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import education.kub.backend.ce.domain.user.model.UserPasswordChangeRequest;
import education.kub.backend.ce.domain.user.model.UserPasswordRecoveryRequest;
import education.kub.backend.ce.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<UserDetailsResponse> getCurrentUser() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var userDetails = userService.getUserWithRoles(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userDetails);
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Void> changeUserPassword(@RequestBody UserPasswordChangeRequest userPasswordChangeRequest) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userService.changeUserPassword(userId, userPasswordChangeRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<Void> recoveryUserPassword(@RequestBody UserPasswordRecoveryRequest userPasswordRecoveryRequest) {
        try {
            userService.recoveryUserPassword(userPasswordRecoveryRequest);
        } catch (Exception ignored) {}



        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
