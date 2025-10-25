package education.kub.backend.ce.domain.academic_title.service;

import education.kub.backend.ce.domain.academic_title.mapper.AcademicTitleMapper;
import education.kub.backend.ce.domain.academic_title.model.AcademicTitleDto;
import education.kub.backend.ce.domain.academic_title.repository.AcademicTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicTitleService {
    private final AcademicTitleRepository academicTitleRepository;

    private final AcademicTitleMapper academicTitleMapper;


    public List<AcademicTitleDto> getAllAcademicTitles(){
        return academicTitleMapper.toDtoList(academicTitleRepository.findAll());
    }
}
