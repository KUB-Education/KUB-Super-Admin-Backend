package education.kub.backend.ce.domain.specialty.mapper;

import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.model.SpecialtyDetailsResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {
    SpecialtyDetailsResponse toDetailsResponse(SpecialtyEntity entity);

    List<SpecialtyDetailsResponse> toDetailsResponseList(Iterable<SpecialtyEntity> entities);
}
