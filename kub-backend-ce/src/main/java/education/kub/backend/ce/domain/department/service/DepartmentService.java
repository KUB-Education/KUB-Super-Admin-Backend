package education.kub.backend.ce.domain.department.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.app.exception.model.KubException.ErrorCode;
import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.model.DepartmentRequest;
import education.kub.backend.ce.domain.department.model.DepartmentUpdateRequest;
import education.kub.backend.ce.domain.department.repository.DepartmentRepository;
import education.kub.backend.ce.domain.department.model.DepartmentResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService{
    private final DepartmentRepository departmentRepository;

    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {
        if (departmentRepository.existsByName(departmentRequest.name())) {
            throw new KubException(ErrorCode.CONFLICT);
        }

        DepartmentEntity newDepartment = new DepartmentEntity();
        newDepartment.setName(departmentRequest.name());

        DepartmentEntity savedDepartment = departmentRepository.save(newDepartment);

        return convertToDTO(savedDepartment);
    }

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<DepartmentResponse> getDepartmentsByNameContaining(String nameSubstring) {
        return departmentRepository.findByNameContainingIgnoreCase(nameSubstring).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Optional<DepartmentResponse> getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<DepartmentResponse> updateDepartment(Long id, DepartmentUpdateRequest departmentUpdateRequest) {
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

                    return convertToDTO(updatedDepartment);
                });
    }

    public boolean deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private DepartmentResponse convertToDTO(DepartmentEntity department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName()
        );
    }
}
