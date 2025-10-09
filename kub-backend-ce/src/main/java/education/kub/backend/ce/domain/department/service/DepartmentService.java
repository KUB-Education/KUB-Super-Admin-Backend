package education.kub.backend.ce.domain.department.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.mapper.DepartmentMapper;
import education.kub.backend.ce.domain.department.model.DepartmentCreateRequest;
import education.kub.backend.ce.domain.department.model.DepartmentDetailsResponse;
import education.kub.backend.ce.domain.department.model.DepartmentUpdateRequest;
import education.kub.backend.ce.domain.department.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService{

    private final DepartmentMapper departmentMapper;

    private final DepartmentRepository departmentRepository;

    public DepartmentDetailsResponse createDepartment(DepartmentCreateRequest departmentCreateRequest) {
        if (departmentRepository.existsByName(departmentCreateRequest.name())) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        var department = new DepartmentEntity();
        department.setName(departmentCreateRequest.name());

        departmentRepository.save(department);

        return departmentMapper.toDetailsResponse(department);
    }

    public List<DepartmentDetailsResponse> getAllDepartments() {
        var departments = departmentRepository.findAll();

        return departmentMapper.toDetailsResponseList(departments);
    }

    public List<DepartmentDetailsResponse> getDepartmentsByNameContaining(String nameSubstring) {
        var departments = departmentRepository.findByNameContainingIgnoreCase(nameSubstring);

        return departmentMapper.toDetailsResponseList(departments);
    }

    public DepartmentDetailsResponse getDepartmentById(Long id) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return departmentMapper.toDetailsResponse(department);
    }

    public DepartmentDetailsResponse updateDepartment(Long id, DepartmentUpdateRequest departmentUpdateRequest) {
        if (departmentRepository.existsByName(departmentUpdateRequest.name())) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        department.setName(departmentUpdateRequest.name());
        departmentRepository.save(department);

        return departmentMapper.toDetailsResponse(department);
    }

    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        departmentRepository.deleteById(id);
    }
}
