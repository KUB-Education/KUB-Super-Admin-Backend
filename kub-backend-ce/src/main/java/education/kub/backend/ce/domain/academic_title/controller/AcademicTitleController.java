package education.kub.backend.ce.domain.academic_title.controller;

import education.kub.backend.ce.domain.academic_title.model.AcademicTitleDto;
import education.kub.backend.ce.domain.academic_title.service.AcademicTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic-titles")
@RequiredArgsConstructor
public class AcademicTitleController {
    private final AcademicTitleService academicTitleService;


    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<AcademicTitleDto>> getAllAcademicTitles() {
        return ResponseEntity.ok(academicTitleService.getAllAcademicTitles());
    }
}
