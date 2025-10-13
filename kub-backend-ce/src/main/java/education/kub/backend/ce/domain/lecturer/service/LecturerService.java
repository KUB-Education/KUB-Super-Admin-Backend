package education.kub.backend.ce.domain.lecturer.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.academic_title.entity.AcademicTitleEntity;
import education.kub.backend.ce.domain.academic_title.repository.AcademicTitleRepository;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.mapper.LecturerMapper;
import education.kub.backend.ce.domain.lecturer.model.*;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;
import education.kub.backend.ce.domain.lecturer_department_position.entity.LecturerDepartmentPositionEntity;
import education.kub.backend.ce.domain.lecturer_department_position.model.LecturerDepartmentPositionCreateRequest;
import education.kub.backend.ce.domain.lecturer_department_position.service.LecturerDepartmentPositionService;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.user.model.UserCreateRequest;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final UserRepository userRepository;

    private final LecturerRepository lecturerRepository;

    private final UserService userService;

    private final LecturerMapper lecturerMapper;

    private final LecturerDepartmentPositionService lecturerDepartmentPositionService;

    private final AcademicTitleRepository academicTitleRepository;


    @Transactional
    public LecturerDetailsResponse createLecturer(LecturerCreateRequest lecturerCreateRequest) {
        // create corresponding user
        UserDetailsResponse user = userService.createUser(
                new UserCreateRequest(lecturerCreateRequest.lastName(), lecturerCreateRequest.firstName(),
                        lecturerCreateRequest.middleName(), lecturerCreateRequest.email()));

        // add LECTURER role
        userService.addUserRole(user.id(), RoleEntity.Type.LECTURER);

        // actually create lecturer
        LecturerEntity lecturer = new LecturerEntity();
        lecturer.setUser(userRepository.findWithRolesById(user.id()).get());
        lecturerRepository.save(lecturer);
        // error in Optional.get() results in Exception -> HTTP 500, it is correct

        return lecturerMapper.toDetailsResponse(lecturer);
    }

    public List<LecturerDetailsResponse> getAllLecturers() {
        return lecturerMapper.toDetailsResponseList(lecturerRepository.findAll());
    }

    public LecturerDetailsResponse getLecturerFullById(Long id) {
        return lecturerMapper.toDetailsResponse(lecturerRepository.findFullEntityById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND)));
    }

    public LecturerDetailsResponse getLecturerByUserId(Long userId) {
        return lecturerMapper.toDetailsResponse(lecturerRepository.findFullEntityByUserId(userId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND)));
    }


    public LecturerDetailsResponse addDepartmentPosition(Long lecturerId,
                                                         LecturerAddDepartmentPositionRequest addRequest) {
        lecturerDepartmentPositionService.createLecturerDepartmentPosition(new LecturerDepartmentPositionCreateRequest(
                lecturerId,
                addRequest.departmentId(),
                addRequest.positionId(),
                LecturerDepartmentPositionEntity.Status.ACTIVE
        ));

        return getLecturerFullById(lecturerId);
    }

    public LecturerDetailsResponse addAcademicTitle(Long lecturerId,
                                                    LecturerAddAcademicTitleRequest addRequest) {
        LecturerEntity lecturer = lecturerRepository.findFullEntityById(lecturerId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        AcademicTitleEntity academicTitle = academicTitleRepository.findById(addRequest.academicTitleId())
                        .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        if(lecturer.hasAcademicTitle(academicTitle)){
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        lecturer.addAcademicTitle(academicTitle);
        lecturerRepository.save(lecturer);

        return lecturerMapper.toDetailsResponse(lecturer);
    }

    // use case: when lecturer updates his academic title from candidate to doctor os science
    @Transactional
    public LecturerDetailsResponse replaceAcademicTitle(Long lecturerId,
                                                    LecturerReplaceAcademicTitleRequest replaceRequest) {
        LecturerEntity lecturer = lecturerRepository.findFullEntityById(lecturerId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        AcademicTitleEntity oldAcademicTitle = academicTitleRepository.findById(replaceRequest.oldAcademicTitleId())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        AcademicTitleEntity newAcademicTitle = academicTitleRepository.findById(replaceRequest.newAcademicTitleId())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        if(!lecturer.hasAcademicTitle(oldAcademicTitle)){
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }
        if(lecturer.hasAcademicTitle(newAcademicTitle)){
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        lecturer.removeAcademicTitle(oldAcademicTitle);
        lecturer.addAcademicTitle(newAcademicTitle);
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
        LecturerEntity lecturer = lecturerRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        Long userId = lecturer.getUser().getId();

        lecturerRepository.delete(lecturer);

        userService.removeUserRole(userId, RoleEntity.Type.LECTURER);
    }
}
