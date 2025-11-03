package education.kub.backend.ce.domain.lecturer.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.academic_title.entity.AcademicTitleEntity;
import education.kub.backend.ce.domain.academic_title.repository.AcademicTitleRepository;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.mapper.LecturerMapper;
import education.kub.backend.ce.domain.lecturer.model.*;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;
import education.kub.backend.ce.domain.lecturer_department_position.model.LecturerDepartmentPositionCreateDto;
import education.kub.backend.ce.domain.lecturer_department_position.model.LecturerDepartmentPositionUpdateDto;
import education.kub.backend.ce.domain.lecturer_department_position.repository.LecturerDepartmentPositionRepository;
import education.kub.backend.ce.domain.lecturer_department_position.service.LecturerDepartmentPositionService;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.user.model.UserCreateRequest;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import education.kub.backend.ce.domain.user.service.UserRoleService;
import education.kub.backend.ce.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;

    private final UserService userService;

    private final UserRoleService userRoleService;

    private final LecturerMapper lecturerMapper;

    private final LecturerDepartmentPositionService lecturerDepartmentPositionService;

    private final AcademicTitleRepository academicTitleRepository;

    private final LecturerDepartmentPositionRepository lecturerDepartmentPositionRepository;


    @Transactional
    public LecturerDetailsResponse createLecturer(LecturerCreateRequest lecturerCreateRequest) {
        UserDetailsResponse user = userService.createUser(
                new UserCreateRequest(lecturerCreateRequest.lastName(), lecturerCreateRequest.firstName(),
                        lecturerCreateRequest.middleName(), lecturerCreateRequest.email()));

        userRoleService.addUserRoleByType(user.id(), RoleEntity.Type.LECTURER);

        return getLecturerByUserId(user.id());
    }

    public List<LecturerDetailsResponse> getAllLecturers() {
        return lecturerMapper.toDetailsResponseList(lecturerRepository.findAll());
    }

    public LecturerDetailsResponse getLecturerById(Long id) {
        return lecturerMapper.toDetailsResponse(lecturerRepository.findFullEntityById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND)));
    }

    public LecturerDetailsResponse getLecturerByUserId(Long userId) {
        return lecturerMapper.toDetailsResponse(lecturerRepository.findFullEntityByUserId(userId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND)));
    }

    public LecturerDetailsResponse addDepartmentPosition(Long lecturerId,
                                                         LecturerAddDepartmentPositionRequest addRequest) {
        lecturerDepartmentPositionService.createLecturerDepartmentPosition(new LecturerDepartmentPositionCreateDto(
                lecturerId,
                addRequest.departmentId(),
                addRequest.positionId()
        ));

        return getLecturerById(lecturerId);
    }

    public LecturerDetailsResponse updateDepartmentPosition(Long lecturerId, Long departmentPositionId,
                                                            LecturerDepartmentPositionUpdateDto updateRequest){
        // check if given Lecturer has given LecturerDepartmentPosition
        if(!lecturerDepartmentPositionRepository.existsByIdAndLecturerId(departmentPositionId, lecturerId)){
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        lecturerDepartmentPositionService.updateLecturerDepartmentPosition(departmentPositionId, updateRequest);

        return getLecturerById(lecturerId);
    }

    public LecturerDetailsResponse deleteDepartmentPosition(Long lecturerId, Long departmentPositionId){
        // check if given Lecturer has given LecturerDepartmentPosition
        if(!lecturerDepartmentPositionRepository.existsByIdAndLecturerId(departmentPositionId, lecturerId)){
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        lecturerDepartmentPositionService.deleteLecturerDepartmentPosition(departmentPositionId);

        return getLecturerById(lecturerId);
    }

    public LecturerDetailsResponse addAcademicTitle(Long lecturerId, Long academicTitleId) {
        LecturerEntity lecturer = lecturerRepository.findFullEntityById(lecturerId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        AcademicTitleEntity academicTitle = academicTitleRepository.findById(academicTitleId)
                        .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        if(lecturer.hasAcademicTitle(academicTitle)){
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        lecturer.addAcademicTitle(academicTitle);
        lecturerRepository.save(lecturer);

        return lecturerMapper.toDetailsResponse(lecturer);
    }

    public LecturerDetailsResponse deleteAcademicTitle(Long lecturerId, Long academicTitleId) {
        LecturerEntity lecturer = lecturerRepository.findFullEntityById(lecturerId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        AcademicTitleEntity academicTitle = academicTitleRepository.findById(academicTitleId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        if(!lecturer.hasAcademicTitle(academicTitle)){
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        lecturer.removeAcademicTitle(academicTitle);
        lecturerRepository.save(lecturer);

        return lecturerMapper.toDetailsResponse(lecturer);
    }

    public void deleteLecturer(Long id) {
        LecturerEntity lecturer = lecturerRepository.findFullEntityById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        userRoleService.removeUserRoleByType(lecturer.getUser().getId(), RoleEntity.Type.LECTURER);
    }
}
