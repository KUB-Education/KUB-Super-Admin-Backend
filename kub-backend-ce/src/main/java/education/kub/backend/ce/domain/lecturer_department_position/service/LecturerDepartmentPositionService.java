package education.kub.backend.ce.domain.lecturer_department_position.service;


import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.repository.DepartmentRepository;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;
import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
import education.kub.backend.ce.domain.lecturer_department_position.mapper.LecturerDepartmentPositionMapper;
import education.kub.backend.ce.domain.lecturer_department_position.model.LecturerDepartmentPositionCreateRequest;
import education.kub.backend.ce.domain.lecturer_department_position.model.LecturerDepartmentPositionDto;
import education.kub.backend.ce.domain.lecturer_department_position.repository.LecturerDepartmentPositionRepository;
import education.kub.backend.ce.domain.position.entity.PositionEntity;
import education.kub.backend.ce.domain.position.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LecturerDepartmentPositionService {
    private final LecturerRepository lecturerRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final LecturerDepartmentPositionRepository lecturerDepartmentPositionRepository;
    private final LecturerDepartmentPositionMapper lecturerDepartmentPositionMapper;

    public LecturerDepartmentPositionDto createLecturerDepartmentPosition(
            LecturerDepartmentPositionCreateRequest createRequest){
        if(lecturerDepartmentPositionRepository.existsByLecturerIdAndDepartmentId(
                createRequest.lecturerId(), createRequest.departmentId())){
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        LecturerEntity lecturer = lecturerRepository.findById(createRequest.lecturerId())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        DepartmentEntity department = departmentRepository.findById(createRequest.departmentId())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        PositionEntity position = positionRepository.findById(createRequest.positionId())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        LecturerDepartmentPositionEntity lecturerDepartmentPosition = new LecturerDepartmentPositionEntity();
        lecturerDepartmentPosition.setLecturer(lecturer);
        lecturerDepartmentPosition.setDepartment(department);
        lecturerDepartmentPosition.setPosition(position);
        lecturerDepartmentPosition.setStatus(createRequest.status());
        lecturerDepartmentPositionRepository.save(lecturerDepartmentPosition);

        return lecturerDepartmentPositionMapper.toDto(lecturerDepartmentPosition);
    }

    public void deleteLecturerDepartmentPosition(Long lecturerId, Long departmentId){
        LecturerDepartmentPositionEntity lecturerDepartmentPositionEntity =
                lecturerDepartmentPositionRepository.findByLecturerIdAndDepartmentId(lecturerId, departmentId)
                        .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        lecturerDepartmentPositionRepository.delete(lecturerDepartmentPositionEntity);
    }
}
