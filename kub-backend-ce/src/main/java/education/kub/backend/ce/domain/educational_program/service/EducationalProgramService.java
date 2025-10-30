package education.kub.backend.ce.domain.educational_program.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.mapper.EducationalProgramMapper;
import education.kub.backend.ce.domain.educational_program.model.EducationalProgramDetailsResponse;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.model.SpecialtyEducationalProgramCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationalProgramService {
    private final EducationalProgramRepository educationalProgramRepository;

    private final EducationalProgramMapper educationalProgramMapper;


    public EducationalProgramDetailsResponse createEducationalProgram(
            SpecialtyEntity specialty,
            SpecialtyEducationalProgramCreateRequest request) {
        if(educationalProgramRepository.existsByName(request.name())) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        var educationalProgram = new EducationalProgramEntity();
        educationalProgram.setSpecialty(specialty);
        educationalProgram.setName(request.name());
        educationalProgram.setDegreeType(request.degreeType());
        educationalProgram.setStudyForm(request.studyForm());
        educationalProgram.setDuration(request.duration());

        educationalProgramRepository.save(educationalProgram);

        return educationalProgramMapper.toDetailsResponse(educationalProgram);
    }
}
