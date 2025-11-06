package education.kub.backend.ce.domain.user.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.role.repository.RoleRepository;
import education.kub.backend.ce.domain.student.entity.StudentEntity;
import education.kub.backend.ce.domain.student.repository.StudentRepository;
import education.kub.backend.ce.domain.user.mapper.UserMapper;
import education.kub.backend.ce.domain.user.model.UserDetailsResponse;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final LecturerRepository lecturerRepository;

    private final StudentRepository studentRepository;

    private final TokenStoreService tokenStoreService;

    public UserDetailsResponse addUserRoleById(Long userId, Long roleId) {
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new KubException((KubException.ErrorCode.NOT_FOUND)));

        return addUserRoleByType(userId, role.getType());
    }

    public UserDetailsResponse removeUserRoleById(Long userId, Long roleId) {
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new KubException((KubException.ErrorCode.NOT_FOUND)));

        return removeUserRoleByType(userId, role.getType());
    }

    public UserDetailsResponse addUserRoleByType(Long userId, RoleEntity.Type type) {
        var user = userRepository.findWithRolesById(userId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        var role = roleRepository.findByType(type)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        user.addRole(role);

        userRepository.save(user);

        if (role.getType() == RoleEntity.Type.LECTURER) {
            LecturerEntity lecturer = new LecturerEntity();
            lecturer.setUser(user);
            lecturerRepository.save(lecturer);
        } else if (role.getType() == RoleEntity.Type.STUDENT) {
            StudentEntity student = new StudentEntity();
            student.setUser(user);
            studentRepository.save(student);
        }

        tokenStoreService.deleteAllSessions(userId);

        return userMapper.toDetailsResponse(user);
    }

    public UserDetailsResponse removeUserRoleByType(Long userId, RoleEntity.Type type) {
        var user = userRepository.findWithRolesById(userId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        var role = roleRepository.findByType(type)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        user.removeRole(role);

        userRepository.save(user);

        if (role.getType() == RoleEntity.Type.LECTURER) {
            LecturerEntity lecturer = lecturerRepository.findFullEntityByUserId(userId)
                    .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

            lecturerRepository.delete(lecturer);
        } else if (role.getType() == RoleEntity.Type.STUDENT) {
            StudentEntity student = studentRepository.findFullEntityByUserId(userId)
                    .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

            studentRepository.delete(student);
        }

        tokenStoreService.deleteAllSessions(userId);

        return userMapper.toDetailsResponse(user);
    }
}
