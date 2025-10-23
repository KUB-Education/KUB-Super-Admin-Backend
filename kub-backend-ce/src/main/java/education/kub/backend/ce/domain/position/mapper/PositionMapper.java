package education.kub.backend.ce.domain.position.mapper;

import education.kub.backend.ce.domain.position.entity.PositionEntity;
import education.kub.backend.ce.domain.position.model.PositionDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    PositionDto toDto(PositionEntity entity);

    List<PositionDto> toDtoList(Iterable<PositionEntity> entities);
}