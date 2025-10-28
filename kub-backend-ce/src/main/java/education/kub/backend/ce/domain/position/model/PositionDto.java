package education.kub.backend.ce.domain.position.model;

import education.kub.backend.ce.domain.position.entity.PositionEntity;

public record PositionDto(
        Long id,

        PositionEntity.PositionName name
) {
}
