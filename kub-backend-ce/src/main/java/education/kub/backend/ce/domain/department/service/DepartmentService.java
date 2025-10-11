package education.kub.backend.ce.domain.department.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.app.exception.model.KubException.ErrorCode;
import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.mapper.DepartmentMapper;
import education.kub.backend.ce.domain.department.model.DepartmentDetailsResponse;
import education.kub.backend.ce.domain.department.model.DepartmentRequest;
import education.kub.backend.ce.domain.department.model.DepartmentUpdateRequest;
import education.kub.backend.ce.domain.department.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService{

    private final DepartmentMapper departmentMapper;

    private final DepartmentRepository departmentRepository;

    public DepartmentDetailsResponse createDepartment(DepartmentRequest departmentRequest) {
        if (departmentRepository.existsByName(departmentRequest.name())) {
            throw new KubException(ErrorCode.CONFLICT);
        }

        DepartmentEntity newDepartment = new DepartmentEntity();
        newDepartment.setName(departmentRequest.name());

        DepartmentEntity savedDepartment = departmentRepository.save(newDepartment);

        return departmentMapper.toDetailsResponse(savedDepartment);
    }

    public List<DepartmentDetailsResponse> getAllDepartments() {
        return departmentMapper.toDetailsResponseList(departmentRepository.findAll());
    }

    public List<DepartmentDetailsResponse> getDepartmentsByNameContaining(String nameSubstring) {
        return departmentMapper.toDetailsResponseList(departmentRepository.findByNameContainingIgnoreCase(nameSubstring));
    }

    public Optional<DepartmentDetailsResponse> getDepartmentById(Long id) {
        return Optional.of(
                departmentMapper.toDetailsResponse(
                        departmentRepository.findById(id)
                                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND))
                )
        );
    }

    public Optional<DepartmentDetailsResponse> updateDepartment(Long id, DepartmentUpdateRequest departmentUpdateRequest) {
        return departmentRepository.findById(id)
                .map(department -> {
                    if (departmentUpdateRequest.name() != null &&
                            !department.getName().equals(departmentUpdateRequest.name()) &&
                            departmentRepository.existsByName(departmentUpdateRequest.name())) {
                        throw new KubException(ErrorCode.CONFLICT);
                    }

                    if (departmentUpdateRequest.name() != null) {
                        department.setName(departmentUpdateRequest.name());
                    }

                    DepartmentEntity updatedDepartment = departmentRepository.save(department);

                    return departmentMapper.toDetailsResponse(updatedDepartment);
                });
    }

    public boolean deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
