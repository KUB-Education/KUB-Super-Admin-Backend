package education.kub.backend.ce.domain.role.controller;

import education.kub.backend.ce.domain.role.model.RoleDetailsResponse;
import education.kub.backend.ce.domain.role.service.RoleService;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<List<RoleDetailsResponse>> getAllRoles() {
        var roles = roleService.getRoles();

        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }
}
