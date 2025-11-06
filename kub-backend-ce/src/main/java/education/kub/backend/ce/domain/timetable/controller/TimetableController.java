package education.kub.backend.ce.domain.timetable.controller;

import education.kub.backend.ce.domain.timetable.model.TimetableCreateRequest;
import education.kub.backend.ce.domain.timetable.model.TimetableDetailsResponse;
import education.kub.backend.ce.domain.timetable.model.TimetableUpdateRequest;
import education.kub.backend.ce.domain.timetable.service.TimetableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timetables")
@RequiredArgsConstructor
public class TimetableController {
    private final TimetableService timetableService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TimetableDetailsResponse> createTimetable(@Valid @RequestBody TimetableCreateRequest request) {
        TimetableDetailsResponse createdTimetable = timetableService.createTimetable(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTimetable);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<TimetableDetailsResponse>> getAllSpecialties() {
        List<TimetableDetailsResponse> specialties = timetableService.getAllSpecialties();

        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<TimetableDetailsResponse> getTimetableById(@PathVariable Long id) {
        var timetableDetails = timetableService.getTimetableById(id);

        return ResponseEntity.status(HttpStatus.OK).body(timetableDetails);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TimetableDetailsResponse> updateTimetable(@PathVariable Long id, @Valid @RequestBody TimetableUpdateRequest request) {
        var timetableDetails = timetableService.updateTimetable(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(timetableDetails);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
