package education.kub.backend.ce.domain.bootstrap.controller;

import education.kub.backend.ce.domain.bootstrap.service.BootstrapGate;
import education.kub.backend.ce.domain.bootstrap.service.BootstrapService;
import education.kub.backend.ce.domain.user.model.UserCreateRequest;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bootstrap")
@RequiredArgsConstructor
public class BootstrapController {

    private final BootstrapGate bootstrapGate;

    private final BootstrapService bootstrapService;

    @PostMapping("/organizer-user")
    public ResponseEntity<UserDetailsResponse> createOrganizerUser(
            @RequestHeader("X-Bootstrap-Token") @NotBlank String token,
            @RequestBody UserCreateRequest userCreateRequest
    ) {
        if (bootstrapGate.checkAccess(token)) {
            var userDetailsResponse = bootstrapService.createOrganizerUser(userCreateRequest);

            bootstrapGate.closeAccess();

            return ResponseEntity.status(HttpStatus.CREATED).body(userDetailsResponse);
        } else {
            return ResponseEntity.status(HttpStatus.GONE).body(null);
        }
    }
}
