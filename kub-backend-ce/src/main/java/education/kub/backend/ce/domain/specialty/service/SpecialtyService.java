package education.kub.backend.ce.domain.specialty.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.educational_program.mapper.EducationalProgramMapper;
import education.kub.backend.ce.domain.educational_program.model.EducationalProgramDetailsResponse;
import education.kub.backend.ce.domain.educational_program.service.EducationalProgramService;
import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.mapper.SpecialtyMapper;
import education.kub.backend.ce.domain.specialty.model.SpecialtyDetailsResponse;
import education.kub.backend.ce.domain.specialty.model.SpecialtyEducationalProgramCreateRequest;
import education.kub.backend.ce.domain.specialty.model.SpecialtyUpdateRequest;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.model.StudyFieldSpecialtyCreateRequest;
import education.kub.backend.ce.domain.study_field.repository.StudyFieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyMapper specialtyMapper;

    private final SpecialtyRepository specialtyRepository;

    private final StudyFieldRepository studyFieldRepository;

    private final EducationalProgramService educationalProgramService;

    private final EducationalProgramMapper educationalProgramMapper;


    public SpecialtyDetailsResponse createSpecialty(StudyFieldEntity studyField,
                                                    StudyFieldSpecialtyCreateRequest request) {
        if (specialtyRepository.existsByCode(request.code())) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        var specialty = new SpecialtyEntity();
        specialty.setStudyField(studyField);
        specialty.setCode(request.code());
        specialty.setName(request.name());

        specialtyRepository.save(specialty);

        return specialtyMapper.toDetailsResponse(specialty);
    }

    public List<SpecialtyDetailsResponse> getAllSpecialties() {
        return specialtyMapper.toDetailsResponseList(specialtyRepository.findAll());
    }

    public SpecialtyDetailsResponse getSpecialtyById(Long id) {
        var specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return specialtyMapper.toDetailsResponse(specialty);
    }

    public SpecialtyDetailsResponse updateSpecialty(Long id, SpecialtyUpdateRequest request) {
        var specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        // update fields
        if(request.studyFieldId() != null){
            var studyField = studyFieldRepository.findById(request.studyFieldId())
                    .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
            specialty.setStudyField(studyField);
        }
        if(request.code() != null){
            if(specialtyRepository.existsByCode(request.code()) &&
                    !specialty.getCode().equals(request.code())){ // check if set the same code as specialty already have
                throw new KubException(KubException.ErrorCode.CONFLICT);
            }
            specialty.setCode(request.code());
        }
        if(request.name() != null){
            specialty.setName(request.name());
        }

        specialtyRepository.save(specialty);

        return specialtyMapper.toDetailsResponse(specialty);
    }

    public void deleteSpecialty(Long id) {
        if (!specialtyRepository.existsById(id)) {
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        specialtyRepository.deleteById(id);
    }


    public EducationalProgramDetailsResponse createEducationalProgram(
            Long specialtyId,
            SpecialtyEducationalProgramCreateRequest request
    ){
        var specialty = specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return educationalProgramService.createEducationalProgram(specialty, request);
    }

    public List<EducationalProgramDetailsResponse> getAllEducationalProgramsForSpecialty(
            Long specialtyId) {
        var specialty = specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return educationalProgramMapper.toDetailsResponseList(specialty.getEducationalPrograms());
    }
}
