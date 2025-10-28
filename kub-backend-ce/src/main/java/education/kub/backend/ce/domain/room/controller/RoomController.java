package education.kub.backend.ce.domain.room.controller;

import education.kub.backend.ce.domain.room.model.RoomCreateRequest;
import education.kub.backend.ce.domain.room.model.RoomDto;
import education.kub.backend.ce.domain.room.model.RoomRequestFilter;
import education.kub.backend.ce.domain.room.model.RoomUpdateRequest;
import education.kub.backend.ce.domain.room.service.RoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody RoomCreateRequest createRoomRequest) {
        RoomDto createdRoomDto = roomService.createRoom(createRoomRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoomDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER', 'STUDENT')")
    public ResponseEntity<List<RoomDto>> getRoomsByFilters(
            @RequestParam(required = false)
            @Size(min = 1, max = 256, message = "Location fragment must be between 1 and 256 characters.")
            String locationContains,

            @RequestParam(required = false)
            @Min(value = 1, message = "Min capacity must be at least 1")
            @Max(value = 32767, message = "Min capacity must be at most 32767")
            Short minCapacity
    ) {
        RoomRequestFilter filter = new RoomRequestFilter(locationContains, minCapacity);
        List<RoomDto> roomsDto = roomService.getRoomsByFilter(filter);

        return ResponseEntity.ok(roomsDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER', 'STUDENT')")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomDto> updateRoom(
            @PathVariable
            Long id,

            @Valid @RequestBody
            RoomUpdateRequest roomUpdateRequest
    ) {
        RoomDto updatedRoom = roomService.updateRoom(id, roomUpdateRequest);

        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        
        return ResponseEntity.ok().build();
    }
}