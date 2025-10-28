package education.kub.backend.ce.domain.lecturer_department_position.service;


import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.department.entity.DepartmentEntity;
import education.kub.backend.ce.domain.department.repository.DepartmentRepository;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;
import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
import education.kub.backend.ce.domain.lecturer_department_position.mapper.LecturerDepartmentPositionMapper;
import education.kub.backend.ce.domain.lecturer_department_position.model.LecturerDepartmentPositionCreateDto;
import education.kub.backend.ce.domain.lecturer_department_position.model.LecturerDepartmentPositionDto;
import education.kub.backend.ce.domain.lecturer_department_position.model.LecturerDepartmentPositionUpdateDto;
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
            LecturerDepartmentPositionCreateDto createRequest
    ){
        if(!lecturerRepository.existsById(createRequest.lecturerId())){
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }
        if(lecturerDepartmentPositionRepository.existsByLecturerIdAndDepartmentId(
                createRequest.lecturerId(), createRequest.departmentId())){
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        LecturerEntity lecturerRef = lecturerRepository.getReferenceById(createRequest.lecturerId());
        DepartmentEntity department = departmentRepository.findById(createRequest.departmentId())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        PositionEntity position = positionRepository.findById(createRequest.positionId())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        LecturerDepartmentPositionEntity lecturerDepartmentPosition = new LecturerDepartmentPositionEntity();
        lecturerDepartmentPosition.setLecturer(lecturerRef);
        lecturerDepartmentPosition.setDepartment(department);
        lecturerDepartmentPosition.setPosition(position);
        lecturerDepartmentPosition.setStatus(LecturerDepartmentPositionEntity.Status.ACTIVE);
        
        lecturerDepartmentPositionRepository.save(lecturerDepartmentPosition);

        return lecturerDepartmentPositionMapper.toDto(lecturerDepartmentPosition);
    }

    public LecturerDepartmentPositionDto updateLecturerDepartmentPosition(
            Long id, LecturerDepartmentPositionUpdateDto updateRequest
    ){
        LecturerDepartmentPositionEntity lecturerDepartmentPosition =
                lecturerDepartmentPositionRepository.findById(id)
                        .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        // update
        if(updateRequest.status() != null){
            lecturerDepartmentPosition.setStatus(updateRequest.status());
        }
        if(updateRequest.positionId() != null){
            PositionEntity position = positionRepository.findById(updateRequest.positionId())
                    .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

            lecturerDepartmentPosition.setPosition(position);
        }

        lecturerDepartmentPositionRepository.save(lecturerDepartmentPosition);

        return lecturerDepartmentPositionMapper.toDto(lecturerDepartmentPosition);
    }

    public void deleteLecturerDepartmentPosition(Long id){
        LecturerDepartmentPositionEntity lecturerDepartmentPosition =
                lecturerDepartmentPositionRepository.findById(id)
                        .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        lecturerDepartmentPositionRepository.delete(lecturerDepartmentPosition);
    }

    public void deleteLecturerDepartmentPosition(Long lecturerId, Long departmentId){
        LecturerDepartmentPositionEntity lecturerDepartmentPosition =
                lecturerDepartmentPositionRepository.findByLecturerIdAndDepartmentId(lecturerId, departmentId)
                        .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        lecturerDepartmentPositionRepository.delete(lecturerDepartmentPosition);
    }
}
