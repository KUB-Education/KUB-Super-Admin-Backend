package education.kub.backend.ce.domain.term.mapper;

import education.kub.backend.ce.domain.term.domain.TermEntity;
import education.kub.backend.ce.domain.term.model.TermDetailsResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TermMapper {
    TermDetailsResponse toDetailsResponse(TermEntity entity);
    List<TermDetailsResponse> toDetailsResponseList(Iterable<TermEntity> entities);
}
