package education.kub.backend.ce.domain.educational_program.controller;

import education.kub.backend.ce.domain.educational_program.model.EducationalProgramDetailsResponse;
import education.kub.backend.ce.domain.educational_program.model.EducationalProgramUpdateRequest;
import education.kub.backend.ce.domain.educational_program.service.EducationalProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/educational-programs")
@RequiredArgsConstructor
public class EducationalProgramController {

    private final EducationalProgramService educationalProgramService;


    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<EducationalProgramDetailsResponse>> getAllEducationalPrograms() {
        List<EducationalProgramDetailsResponse> educationalPrograms
                = educationalProgramService.getAllEducationalPrograms();

        return ResponseEntity.ok(educationalPrograms);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<EducationalProgramDetailsResponse> getEducationalProgramById(@PathVariable Long id) {
        var educationalProgram = educationalProgramService.getEducationalProgramById(id);

        return ResponseEntity.status(HttpStatus.OK).body(educationalProgram);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EducationalProgramDetailsResponse> updateEducationalProgram(
            @PathVariable Long id,
            @Valid @RequestBody EducationalProgramUpdateRequest request) {
        var updatedEducationalProgram = educationalProgramService.updateEducationalProgram(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(updatedEducationalProgram);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteEducationalProgram(@PathVariable Long id) {
        educationalProgramService.deleteEducationalProgram(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
