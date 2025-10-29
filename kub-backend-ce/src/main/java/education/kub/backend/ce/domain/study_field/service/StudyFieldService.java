package education.kub.backend.ce.domain.study_field.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.specialty.model.SpecialtyDetailsResponse;
import education.kub.backend.ce.domain.specialty.service.SpecialtyService;
import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.mapper.StudyFieldMapper;
import education.kub.backend.ce.domain.study_field.model.StudyFieldCreateRequest;
import education.kub.backend.ce.domain.study_field.model.StudyFieldDetailsResponse;
import education.kub.backend.ce.domain.study_field.model.StudyFieldSpecialtyCreateRequest;
import education.kub.backend.ce.domain.study_field.model.StudyFieldUpdateRequest;
import education.kub.backend.ce.domain.study_field.repository.StudyFieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyFieldService {

    private final StudyFieldMapper studyFieldMapper;
    private final StudyFieldRepository studyFieldRepository;
    private final SpecialtyService specialtyService;

    public StudyFieldDetailsResponse createStudyField(StudyFieldCreateRequest request) {
        if (studyFieldRepository.existsByCode(request.code())) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        var studyField = new StudyFieldEntity();
        studyField.setCode(request.code());
        studyField.setName(request.name());

        studyFieldRepository.save(studyField);

        return studyFieldMapper.toDetailsResponse(studyField);
    }

    public List<StudyFieldDetailsResponse> getAllStudyFields() {
        return studyFieldMapper.toDetailsResponseList(studyFieldRepository.findAll());
    }

    public List<StudyFieldDetailsResponse> getStudyFieldsByNameContaining(String nameSubstring) {
        return studyFieldMapper.toDetailsResponseList(studyFieldRepository.findByNameContainingIgnoreCase(nameSubstring));
    }

    public StudyFieldDetailsResponse getStudyFieldById(Long id) {
        var studyField = studyFieldRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return studyFieldMapper.toDetailsResponse(studyField);
    }

    public StudyFieldDetailsResponse updateStudyField(Long id, StudyFieldUpdateRequest request) {
        var studyField = studyFieldRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        // update fields
        if(request.code() != null){
            if(studyFieldRepository.existsByCode(request.code())){
                throw new KubException(KubException.ErrorCode.CONFLICT);
            }

            studyField.setCode(request.code());
        }
        if(request.name() != null){
            studyField.setName(request.name());
        }

        studyFieldRepository.save(studyField);

        return studyFieldMapper.toDetailsResponse(studyField);
    }

    public void deleteStudyField(Long id) {
        if (!studyFieldRepository.existsById(id)) {
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        studyFieldRepository.deleteById(id);
    }


    public SpecialtyDetailsResponse createSpecialty(Long studyFieldId,
                                                    StudyFieldSpecialtyCreateRequest request) {
        var studyField = studyFieldRepository.findById(studyFieldId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return specialtyService.createSpecialty(studyField, request);
    }

    public List<SpecialtyDetailsResponse> getAllSpecialtiesForStudyField(Long studyFieldId) {
        if (!studyFieldRepository.existsById(studyFieldId)) {
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        return specialtyService.getAllSpecialitiesForStudyField(studyFieldId);
    }
}
