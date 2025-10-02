package education.kub.backend.ce.domain.room.mapper;

import education.kub.backend.ce.domain.room.domain.RoomEntity;
import education.kub.backend.ce.domain.room.model.RoomDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomDto toDto(RoomEntity entity);

    List<RoomDto> toDtoList(Iterable<RoomEntity> entities);
}
