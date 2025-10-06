package education.kub.backend.ce.domain.department.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.app.exception.model.KubException.ErrorCode;
import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.mapper.DepartmentMapper;
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

    private final DepartmentMapper departmentMapper;

    private final DepartmentRepository departmentRepository;

    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {
        if (departmentRepository.existsByName(departmentRequest.name())) {
            throw new KubException(ErrorCode.CONFLICT);
        }

        DepartmentEntity newDepartment = new DepartmentEntity();
        newDepartment.setName(departmentRequest.name());

        DepartmentEntity savedDepartment = departmentRepository.save(newDepartment);

        return departmentMapper.toDepartmentResponse(savedDepartment);
    }

    public List<DepartmentResponse> getAllDepartments() {
        return departmentMapper.toDepartmentResponseList(departmentRepository.findAll());
    }

    public List<DepartmentResponse> getDepartmentsByNameContaining(String nameSubstring) {
        return departmentMapper.toDepartmentResponseList(departmentRepository.findByNameContainingIgnoreCase(nameSubstring));
    }

    public Optional<DepartmentResponse> getDepartmentById(Long id) {
        return departmentMapper.toDepartmentResponseOptional(departmentRepository.findById(id));
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

                    return departmentMapper.toDepartmentResponse(updatedDepartment);
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
