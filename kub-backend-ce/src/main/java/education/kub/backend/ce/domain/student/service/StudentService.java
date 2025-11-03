package education.kub.backend.ce.domain.student.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.lecturer.model.LecturerCreateRequest;
import education.kub.backend.ce.domain.lecturer.model.LecturerDetailsResponse;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.student.model.StudentDetailsResponse;
import education.kub.backend.ce.domain.user.model.UserCreateRequest;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import education.kub.backend.ce.domain.user.service.UserRoleService;
import education.kub.backend.ce.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final UserService userService;

    private final UserRoleService userRoleService;

    @Transactional
    public StudentDetailsResponse createLecturer(LecturerCreateRequest lecturerCreateRequest) {
        UserDetailsResponse user = userService.createUser(
                new UserCreateRequest(lecturerCreateRequest.lastName(), lecturerCreateRequest.firstName(),
                        lecturerCreateRequest.middleName(), lecturerCreateRequest.email()));

        userRoleService.addUserRoleByType(user.id(), RoleEntity.Type.STUDENT);

        return getStudentByUserId(user.id());
    }

    public LecturerDetailsResponse getLecturerByUserId(Long userId) {
        return lecturerMapper.toDetailsResponse(lecturerRepository.findFullEntityByUserId(userId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND)));
    }
}
