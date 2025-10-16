package education.kub.backend.ce.domain.specialty.controller;

import education.kub.backend.ce.domain.specialty.model.SpecialtyCreateRequest;
import education.kub.backend.ce.domain.specialty.model.SpecialtyDetailsResponse;
import education.kub.backend.ce.domain.specialty.model.SpecialtyUpdateRequest;
import education.kub.backend.ce.domain.specialty.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SpecialtyDetailsResponse> createSpecialty(@Valid @RequestBody SpecialtyCreateRequest request) {
        SpecialtyDetailsResponse createdSpecialty = specialtyService.createSpecialty(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpecialty);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<SpecialtyDetailsResponse>> getAllSpecialties() {
        List<SpecialtyDetailsResponse> specialties = specialtyService.getAllSpecialties();

        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<SpecialtyDetailsResponse> getSpecialtyById(@PathVariable Long id) {
        var specialtyDetails = specialtyService.getSpecialtyById(id);

        return ResponseEntity.status(HttpStatus.OK).body(specialtyDetails);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SpecialtyDetailsResponse> updateSpecialty(@PathVariable Long id, @Valid @RequestBody SpecialtyUpdateRequest request) {
        var specialtyDetails = specialtyService.updateSpecialty(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(specialtyDetails);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        specialtyService.deleteSpecialty(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}