package education.kub.backend.ce.domain.department.controller;

import education.kub.backend.ce.domain.department.model.DepartmentRequest;
import education.kub.backend.ce.domain.department.model.DepartmentResponse;
import education.kub.backend.ce.domain.department.model.DepartmentUpdateRequest;
import education.kub.backend.ce.domain.department.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Validated
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentRequest departmentRequest) {
        DepartmentResponse createdDepartment = departmentService.createDepartment(departmentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER', 'STUDENT')")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments(
            @RequestParam(required = false) String name
    ) {
        if (name != null && !name.isEmpty()) {
            List<DepartmentResponse> filteredDepartments = departmentService.getDepartmentsByNameContaining(name);
            return ResponseEntity.ok(filteredDepartments);
        }
        
        List<DepartmentResponse> departments = departmentService.getAllDepartments();

        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER', 'STUDENT')")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentUpdateRequest departmentUpdateRequest
    ) {
        return departmentService.updateDepartment(id, departmentUpdateRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
