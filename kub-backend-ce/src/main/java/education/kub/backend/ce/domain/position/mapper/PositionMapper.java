package education.kub.backend.ce.domain.position.mapper;

import education.kub.backend.ce.domain.position.entity.PositionEntity;
import education.kub.backend.ce.domain.position.model.PositionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    PositionDto toDto(PositionEntity entity);
}