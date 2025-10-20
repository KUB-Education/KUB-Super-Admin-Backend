package education.kub.backend.ce.domain.study_field.controller;

import education.kub.backend.ce.domain.study_field.model.*;
import education.kub.backend.ce.domain.study_field.service.StudyFieldService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/study-fields")
@RequiredArgsConstructor
@Validated
public class StudyFieldController {

    private final StudyFieldService studyFieldService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StudyFieldDetailsResponse> createStudyField(
            @Valid @RequestBody StudyFieldCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studyFieldService.createStudyField(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<StudyFieldDetailsResponse>> getAllStudyFields(
            @RequestParam(required = false) String name
    ) {
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(studyFieldService.getStudyFieldsByNameContaining(name));
        }
        return ResponseEntity.ok(studyFieldService.getAllStudyFields());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<StudyFieldDetailsResponse> getStudyFieldById(@PathVariable Long id) {
        return ResponseEntity.ok(studyFieldService.getStudyFieldById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StudyFieldDetailsResponse> updateStudyField(
            @PathVariable Long id,
            @Valid @RequestBody StudyFieldUpdateRequest request
    ) {
        return ResponseEntity.ok(studyFieldService.updateStudyField(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteStudyField(@PathVariable Long id) {
        studyFieldService.deleteStudyField(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
