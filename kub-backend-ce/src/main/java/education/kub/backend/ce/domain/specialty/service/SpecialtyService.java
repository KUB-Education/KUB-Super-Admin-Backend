package education.kub.backend.ce.domain.specialty.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.mapper.SpecialtyMapper;
import education.kub.backend.ce.domain.specialty.model.SpecialtyCreateRequest;
import education.kub.backend.ce.domain.specialty.model.SpecialtyDetailsResponse;
import education.kub.backend.ce.domain.specialty.model.SpecialtyUpdateRequest;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyMapper specialtyMapper;

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyDetailsResponse createSpecialty(SpecialtyCreateRequest SpecialtyCreateRequest) {
        if (specialtyRepository.existsByName(SpecialtyCreateRequest.name()) ||
        specialtyRepository.existsByCode(SpecialtyCreateRequest.code())) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        var Specialty = new SpecialtyEntity();
        Specialty.setName(SpecialtyCreateRequest.name());

        specialtyRepository.save(Specialty);

        return specialtyMapper.toDetailsResponse(Specialty);
    }

    public List<SpecialtyDetailsResponse> getAllSpecialties() {
        var Specialties = specialtyRepository.findAll();

        return specialtyMapper.toDetailsResponseList(Specialties);
    }

    public SpecialtyDetailsResponse getSpecialtyById(Long id) {
        var Specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return specialtyMapper.toDetailsResponse(Specialty);
    }

    public SpecialtyDetailsResponse updateSpecialty(Long id, SpecialtyUpdateRequest specialtyUpdateRequest) {
        if (specialtyRepository.existsByName(specialtyUpdateRequest.name()) ||
                specialtyRepository.existsByCode(specialtyUpdateRequest.code())) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        var Specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        Specialty.setName(specialtyUpdateRequest.name());
        Specialty.setCode(specialtyUpdateRequest.code());
        specialtyRepository.save(Specialty);

        return specialtyMapper.toDetailsResponse(Specialty);
    }

    public void deleteSpecialty(Long id) {
        if (!specialtyRepository.existsById(id)) {
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        specialtyRepository.deleteById(id);
    }
}
