package education.kub.superadmin.domain.admin.controller;

import education.kub.superadmin.domain.admin.model.AdminRequestDTO;
import education.kub.superadmin.domain.admin.model.AdminResponseDTO;
import education.kub.superadmin.domain.admin.model.AdminUpdateRequestDTO;
import education.kub.superadmin.domain.admin.entity.AdminEntity;
import education.kub.superadmin.domain.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminResponseDTO> createAdmin(@RequestBody @Valid AdminRequestDTO adminRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(adminRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<AdminResponseDTO>> getAdmins() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> getAdmin(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAdmin(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(
            @PathVariable("id") Long id,

            @RequestBody @Valid AdminUpdateRequestDTO adminUpdateRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.updateAdmin(id, adminUpdateRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable("id") Long id) {
        adminService.deleteAdmin(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/{id}/resend")
    public ResponseEntity<AdminResponseDTO> resendAdminPassword(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.resendAdminPassword(id));
    }
}
