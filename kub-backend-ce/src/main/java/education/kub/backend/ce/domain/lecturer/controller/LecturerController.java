package education.kub.backend.ce.domain.lecturer.controller;

import education.kub.backend.ce.domain.lecturer.model.*;
import education.kub.backend.ce.domain.lecturer.service.LecturerService;
import education.kub.backend.ce.domain.room.model.RoomDto;
import education.kub.backend.ce.domain.room.model.RoomUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lecturers")
@RequiredArgsConstructor
public class LecturerController {
    private final LecturerService lecturerService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<LecturerDetailsResponse> createLecturer(
            @Valid @RequestBody LecturerCreateRequest createLecturerRequest
    ){
        LecturerDetailsResponse createdLecturer = lecturerService.createLecturer(createLecturerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdLecturer);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN', 'LECTURER', 'STUDENT')")
    public ResponseEntity<List<LecturerDetailsResponse>> getAllLecturers() {
        return ResponseEntity.ok(lecturerService.getAllLecturers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN', 'LECTURER', 'STUDENT')")
    public ResponseEntity<LecturerDetailsResponse> getLecturerById(@PathVariable Long id) {
        return ResponseEntity.ok(lecturerService.getLecturerFullById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN', 'LECTURER', 'STUDENT')")
    public ResponseEntity<LecturerDetailsResponse> getLecturerByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(lecturerService.getLecturerByUserId(userId));
    }



    @PostMapping("/{id}/department-positions")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<LecturerDetailsResponse> addDepartmentPosition(
            @PathVariable Long id,
            @Valid @RequestBody LecturerAddDepartmentPositionRequest addRequest
    ){
        LecturerDetailsResponse updatedLecturer = lecturerService.addDepartmentPosition(id, addRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedLecturer);
    }

    @PostMapping("/{id}/academic-titles")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<LecturerDetailsResponse> addAcademicTitle(
            @PathVariable Long id,
            @Valid @RequestBody LecturerAddAcademicTitleRequest addRequest
    ){
        LecturerDetailsResponse updatedLecturer = lecturerService.addAcademicTitle(id, addRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedLecturer);
    }

    @DeleteMapping("/{lecturerId}/academic-titles/{academicTitleId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<LecturerDetailsResponse> deleteAcademicTitle(
            @PathVariable Long lecturerId,
            @PathVariable Long academicTitleId
    ){
        LecturerDetailsResponse updatedLecturer = lecturerService.deleteAcademicTitle(lecturerId, academicTitleId);

        return ResponseEntity.ok().body(updatedLecturer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<Void> deleteLecturer(@PathVariable Long id) {
        lecturerService.deleteLecturer(id);

        return ResponseEntity.ok().build();
    }
}
