package education.kub.backend.ce.domain.educational_program.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.mapper.EducationalProgramMapper;
import education.kub.backend.ce.domain.educational_program.model.EducationalProgramDetailsResponse;
import education.kub.backend.ce.domain.educational_program.model.EducationalProgramUpdateRequest;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.model.SpecialtyEducationalProgramCreateRequest;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationalProgramService {
    private final EducationalProgramRepository educationalProgramRepository;

    private final EducationalProgramMapper educationalProgramMapper;

    private final SpecialtyRepository specialtyRepository;


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

    public List<EducationalProgramDetailsResponse> getAllEducationalPrograms(){
        return educationalProgramMapper.toDetailsResponseList(educationalProgramRepository.findAll());
    }

    public EducationalProgramDetailsResponse getEducationalProgramById(Long id) {
        var educationalProgram = educationalProgramRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return educationalProgramMapper.toDetailsResponse(educationalProgram);
    }

    public EducationalProgramDetailsResponse updateEducationalProgram(Long id, EducationalProgramUpdateRequest request){
        var educationalProgram = educationalProgramRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        // update fields
        if(request.specialtyId() != null){
            var specialty = specialtyRepository.findById(request.specialtyId())
                    .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
            educationalProgram.setSpecialty(specialty);
        }
        if(request.name() != null){ // name is unique across all educational programs
            // check if set the same name as educationalProgram already have
            if(educationalProgramRepository.existsByNameAndIdIsNot(request.name(), id)){
                throw new KubException(KubException.ErrorCode.CONFLICT);
            }
            educationalProgram.setName(request.name());
        }
        if(request.degreeType() != null){
            educationalProgram.setDegreeType(request.degreeType());
        }
        if(request.studyForm() != null){
            educationalProgram.setStudyForm(request.studyForm());
        }
        if(request.duration() != null){
            educationalProgram.setDuration(request.duration());
        }

        educationalProgramRepository.save(educationalProgram);

        return educationalProgramMapper.toDetailsResponse(educationalProgram);
    }

    public void deleteEducationalProgram(Long id) {
        if (!educationalProgramRepository.existsById(id)) {
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        educationalProgramRepository.deleteById(id);
    }

}
