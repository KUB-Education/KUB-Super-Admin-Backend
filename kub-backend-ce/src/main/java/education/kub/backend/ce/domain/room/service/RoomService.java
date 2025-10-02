package education.kub.backend.ce.domain.room.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.room.domain.RoomEntity;
import education.kub.backend.ce.domain.room.mapper.RoomMapper;
import education.kub.backend.ce.domain.room.model.RoomCreateRequest;
import education.kub.backend.ce.domain.room.model.RoomDto;
import education.kub.backend.ce.domain.room.model.RoomRequestFilter;
import education.kub.backend.ce.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepo;
    private final RoomMapper roomMapper;

    public RoomDto createRoom(RoomCreateRequest createRoomRequest) {
        if (roomRepo.existsByLocation(createRoomRequest.location())) { // location must be unique
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        RoomEntity room = new RoomEntity();
        room.setLocation(createRoomRequest.location());
        room.setCapacity(createRoomRequest.capacity());
        room.setDetails(createRoomRequest.details());
        roomRepo.save(room);

        return roomMapper.toDto(room);
    }


    public RoomDto getRoomById(Long id) {
        return roomMapper.toDto(roomRepo.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND)));
    }

    public List<RoomDto> getRoomsByFilter(RoomRequestFilter filter) {
        List<RoomEntity> rooms;

        // check what criterion to apply
        if (filter.locationContains() != null && filter.minCapacity() != null) {
            rooms = roomRepo.findByLocationContainingAndCapacityGreaterThanEqual(
                    filter.locationContains(),
                    filter.minCapacity()
            );
        }
        else if (filter.locationContains() != null) {
            rooms = roomRepo.findByLocationContaining(filter.locationContains());
        }
        else if (filter.minCapacity() != null) {
            rooms = roomRepo.findByCapacityGreaterThanEqual(filter.minCapacity());
        }
        else {
            rooms = roomRepo.findAll();
        }

        return roomMapper.toDtoList(rooms);
    }

    /*
    @Override
    public Optional<RoomResponseDTO> updateRoom(Long id, RoomUpdateRequestDTO roomUpdateRequest) {
        return roomRepo.findById(id).map(room -> {

            if (roomUpdateRequest.location() != null &&
                    !room.getLocation().equals(roomUpdateRequest.location()) &&
                    roomRepo.existsByLocation(roomUpdateRequest.location())) {
                throw new KubException(ErrorCode.CONFLICT);
            }

            if (roomUpdateRequest.location() != null) {
                room.setLocation(roomUpdateRequest.location());
            }
            if (roomUpdateRequest.capacity() != null) {
                room.setCapacity(roomUpdateRequest.capacity());
            }

            RoomEntity updatedRoom = roomRepo.save(room);

            return convertToDTO(updatedRoom);
        });
    }

    @Override
    public boolean deleteRoom(Long id) {
        if (roomRepo.existsById(id)) {
            roomRepo.deleteById(id);
            return true;
        }

        return false;
    }

    private RoomResponseDTO convertToDTO(RoomEntity room) {
        return new RoomResponseDTO(room.getId(), room.getLocation(), room.getCapacity());
    }

     */
}