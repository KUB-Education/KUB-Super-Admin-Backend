package education.kub.backend.ce.domain.lecturer.controller;

import education.kub.backend.ce.domain.lecturer.model.*;
import education.kub.backend.ce.domain.lecturer.service.LecturerService;
import education.kub.backend.ce.domain.lecturer_department_position.model.LecturerDepartmentPositionUpdateRequest;
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
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<LecturerDetailsResponse>> getAllLecturers() {
        return ResponseEntity.ok(lecturerService.getAllLecturers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<LecturerDetailsResponse> getLecturerById(@PathVariable Long id) {
        return ResponseEntity.ok(lecturerService.getLecturerById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('USER')")
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

    @PutMapping("/{lecturerId}/department-positions/{departmentPositionId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<LecturerDetailsResponse> updateDepartmentPosition(
            @PathVariable Long lecturerId,
            @PathVariable Long departmentPositionId,
            @Valid @RequestBody LecturerDepartmentPositionUpdateRequest updateRequest
    ){
        LecturerDetailsResponse updatedLecturer = lecturerService.updateDepartmentPosition(
                lecturerId, departmentPositionId, updateRequest);

        return ResponseEntity.ok(updatedLecturer);
    }

    @DeleteMapping("/{lecturerId}/department-positions/{departmentPositionId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<LecturerDetailsResponse> deleteDepartmentPosition(
            @PathVariable Long lecturerId,
            @PathVariable Long departmentPositionId
    ){
        LecturerDetailsResponse updatedLecturer =
                lecturerService.deleteDepartmentPosition(lecturerId, departmentPositionId);

        return ResponseEntity.ok().body(updatedLecturer);
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

    @PutMapping("/{lecturerId}/academic-titles/{academicTitleId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'SYSADMIN', 'ADMIN')")
    public ResponseEntity<LecturerDetailsResponse> updateAcademicTitle(
            @PathVariable Long lecturerId,
            @PathVariable Long academicTitleId,
            @Valid @RequestBody LecturerUpdateAcademicTitleRequest updateRequest
    ){
        LecturerDetailsResponse updatedLecturer = lecturerService.updateAcademicTitle(
                lecturerId, academicTitleId, updateRequest);

        return ResponseEntity.ok(updatedLecturer);
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
