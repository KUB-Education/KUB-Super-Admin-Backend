package education.kub.backend.ce.domain.position.service;

import education.kub.backend.ce.domain.position.mapper.PositionMapper;
import education.kub.backend.ce.domain.position.model.PositionDto;
import education.kub.backend.ce.domain.position.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;

    private final PositionMapper positionMapper;


    public List<PositionDto> getAllPositions(){
        return positionMapper.toDtoList(positionRepository.findAll());
    }
}
