package education.kub.backend.ce.domain.term.controller;

import education.kub.backend.ce.domain.term.model.TermCreateRequest;
import education.kub.backend.ce.domain.term.model.TermDetailsResponse;
import education.kub.backend.ce.domain.term.model.TermUpdateRequest;
import education.kub.backend.ce.domain.term.service.TermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/terms")
@RequiredArgsConstructor
@Validated
public class TermController {

    private final TermService termService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TermDetailsResponse> createTerm(@Valid @RequestBody TermCreateRequest request) {
        var createdTerm = termService.createTerm(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTerm);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<TermDetailsResponse>> getAllTerms() {
        var terms = termService.getAllTerms();
        return ResponseEntity.ok(terms);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<TermDetailsResponse> getTermById(@PathVariable Long id) {
        var term = termService.getTermById(id);
        return ResponseEntity.ok(term);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TermDetailsResponse> updateTerm(
            @PathVariable Long id,
            @Valid @RequestBody TermUpdateRequest request
    ) {
        var updatedTerm = termService.updateTerm(id, request);
        return ResponseEntity.ok(updatedTerm);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTerm(@PathVariable Long id) {
        termService.deleteTerm(id);
        return ResponseEntity.noContent().build();
    }
}
