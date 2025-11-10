package education.kub.backend.ce.domain.student.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.lecturer.model.LecturerCreateRequest;
import education.kub.backend.ce.domain.lecturer.model.LecturerDetailsResponse;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.student.mapper.StudentMapper;
import education.kub.backend.ce.domain.student.model.StudentCreateRequest;
import education.kub.backend.ce.domain.student.model.StudentShortDetailsResponse;
import education.kub.backend.ce.domain.student.repository.StudentRepository;
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

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    @Transactional
    public StudentShortDetailsResponse createStudent(StudentCreateRequest studentCreateRequest) {
        UserDetailsResponse user = userService.createUser(
                new UserCreateRequest(
                        studentCreateRequest.lastName(),
                        studentCreateRequest.firstName(),
                        studentCreateRequest.middleName(),
                        studentCreateRequest.email()
                )
        );

        userRoleService.addUserRoleByType(user.id(), RoleEntity.Type.STUDENT);

        return getStudentByUserId(user.id());
    }

    public StudentShortDetailsResponse getStudentByUserId(Long userId) {
        return studentMapper.toShortDetailsResponse(studentRepository.findFullEntityByUserId(userId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND)));
    }
}
