package education.kub.backend.ce.domain.department.controller;

import education.kub.backend.ce.domain.department.model.DepartmentCreateRequest;
import education.kub.backend.ce.domain.department.model.DepartmentDetailsResponse;
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
    public ResponseEntity<DepartmentDetailsResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequest departmentCreateRequest) {
        DepartmentDetailsResponse createdDepartment = departmentService.createDepartment(departmentCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<DepartmentDetailsResponse>> getAllDepartments(
            @RequestParam(required = false) String name
    ) {
        if (name != null && !name.isEmpty()) {
            List<DepartmentDetailsResponse> filteredDepartments = departmentService.getDepartmentsByNameContaining(name);
            return ResponseEntity.ok(filteredDepartments);
        }
        
        List<DepartmentDetailsResponse> departments = departmentService.getAllDepartments();

        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<DepartmentDetailsResponse> getDepartmentById(@PathVariable Long id) {
        var departmentDetails = departmentService.getDepartmentById(id);

        return ResponseEntity.status(HttpStatus.OK).body(departmentDetails);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DepartmentDetailsResponse> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentUpdateRequest departmentUpdateRequest
    ) {
        var departmentDetails = departmentService.updateDepartment(id, departmentUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(departmentDetails);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
