package education.kub.backend.ce.domain.group.controller;

import education.kub.backend.ce.domain.group.model.GroupCreateRequest;
import education.kub.backend.ce.domain.group.model.GroupFullDetailsResponse;
import education.kub.backend.ce.domain.group.model.GroupShortDetailsResponse;
import education.kub.backend.ce.domain.group.model.GroupUpdateRequest;
import education.kub.backend.ce.domain.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    final private GroupService groupService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<GroupShortDetailsResponse> createGroup(
            @Valid @RequestBody GroupCreateRequest groupCreateRequest
    ) {
        var group = groupService.createGroup(groupCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<GroupShortDetailsResponse>> getAllGroups() {
        var groups = groupService.getAllGroups();

        return ResponseEntity.status(HttpStatus.OK).body(groups);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<GroupFullDetailsResponse> getGroup(@PathVariable Long id) {
        var group = groupService.getGroupById(id);

        return ResponseEntity.status(HttpStatus.OK).body(group);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<GroupShortDetailsResponse> updateGroup(
            @PathVariable Long id,
            @Valid @RequestBody GroupUpdateRequest groupUpdateRequest
    ) {
        var group = groupService.updateGroup(id, groupUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(group);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PutMapping("/{groupId}/students/{studentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<GroupFullDetailsResponse> addStudentToGroup(
            @PathVariable Long groupId,
            @PathVariable Long studentId
    ) {
        var group = groupService.addStudentToGroup(groupId, studentId);

        return ResponseEntity.status(HttpStatus.OK).body(group);
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<GroupFullDetailsResponse> removeStudentFromGroup(
            @PathVariable Long groupId,
            @PathVariable Long studentId
    ) {
        var group = groupService.removeStudentFromGroup(groupId, studentId);

        return ResponseEntity.status(HttpStatus.OK).body(group);
    }
}
