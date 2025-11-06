package education.kub.backend.ce.domain.room.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.room.entity.RoomEntity;
import education.kub.backend.ce.domain.room.mapper.RoomMapper;
import education.kub.backend.ce.domain.room.model.RoomCreateRequest;
import education.kub.backend.ce.domain.room.model.RoomDto;
import education.kub.backend.ce.domain.room.model.RoomRequestFilter;
import education.kub.backend.ce.domain.room.model.RoomUpdateRequest;
import education.kub.backend.ce.domain.room.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepo;
    private final RoomMapper roomMapper;

    @Transactional
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
        } else if (filter.locationContains() != null) {
            rooms = roomRepo.findByLocationContaining(filter.locationContains());
        } else if (filter.minCapacity() != null) {
            rooms = roomRepo.findByCapacityGreaterThanEqual(filter.minCapacity());
        } else {
            rooms = roomRepo.findAll();
        }

        return roomMapper.toDtoList(rooms);
    }

    @Transactional
    public RoomDto updateRoom(Long id, RoomUpdateRequest roomUpdateRequest) {
        RoomEntity room = roomRepo.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        if (roomUpdateRequest.location() != null) {
            if (roomRepo.existsByLocationAndIdNot(roomUpdateRequest.location(), room.getId())) {
                throw new KubException(KubException.ErrorCode.CONFLICT);
            }

            room.setLocation(roomUpdateRequest.location());
        }

        if (roomUpdateRequest.capacity() != null) {
            room.setCapacity(roomUpdateRequest.capacity());
        }

        if (roomUpdateRequest.details() != null) {
            if (roomUpdateRequest.details().isEmpty()) {
                room.setDetails(null);
            } else {
                room.setDetails(roomUpdateRequest.details());
            }
        }

        roomRepo.save(room);

        return roomMapper.toDto(room);
    }


    public void deleteRoom(Long id) {
        RoomEntity room = roomRepo.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        roomRepo.delete(room);
    }

}