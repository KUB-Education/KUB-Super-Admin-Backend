package education.kub.superadmin.controller;

import education.kub.superadmin.dto.AdminRequestDTO;
import education.kub.superadmin.dto.AdminResponseDTO;
import education.kub.superadmin.dto.AdminUpdateRequestDTO;
import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.services.impl.AdminServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import java.util.List;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServiceImpl adminService;

    /**
     * Creates a new admin with the provided details
     * @param adminRequest
     */
    @PostMapping
    public ResponseEntity<AdminResponseDTO> createAdmin(@RequestBody @Valid AdminRequestDTO adminRequest) {
        final AdminEntity adminEntity = adminService.createAdmin(adminRequest);
        final AdminResponseDTO adminResponseDTO = adminEntity.toAdminResponseDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(adminResponseDTO);
    }

    /**
     * Retrieves a list of admins
     */
    @GetMapping
    public ResponseEntity<List<AdminResponseDTO>> getAdmins() {
        final List<AdminEntity> allAdmins = adminService.getAllAdmins();
        final List<AdminResponseDTO> allAdminsResponse = allAdmins.stream().map(AdminEntity::toAdminResponseDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(allAdminsResponse);
    }

    /**
     * Retrieves details of a specific admin
     * @param id
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> getAdmin(@PathVariable("id") Long id) {
        final AdminEntity adminById = adminService.getAdminById(id);
        final AdminResponseDTO adminResponseDTO = adminById.toAdminResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(adminResponseDTO);
    }

    /**
     * Updates an admin's details
     * @param id
     * @param updateRequest
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(@PathVariable("id") Long id,
                                                        @RequestBody @Valid AdminUpdateRequestDTO updateRequest) {
        final AdminEntity updatedAdminEntity = adminService.updateAdmin(id, updateRequest);
        final AdminResponseDTO adminResponseDTO = updatedAdminEntity.toAdminResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(adminResponseDTO);
    }

    /**
     * Deletes an admin
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable("id") Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Resends password to the admin email. Returns 503 if the SMTP component fails
     * @param id
     * @throws ServiceUnavailableException
     */
    @PostMapping("/{id}/resend")
    public ResponseEntity<AdminResponseDTO> resendAdmin(@PathVariable("id") Long id) throws ServiceUnavailableException {
        adminService.resendTemporaryPassword(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
