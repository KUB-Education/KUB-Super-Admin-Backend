package education.kub.backend.ce.domain.group.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.group.entity.GroupEntity;
import education.kub.backend.ce.domain.group.mapper.GroupMapper;
import education.kub.backend.ce.domain.group.model.GroupCreateRequest;
import education.kub.backend.ce.domain.group.model.GroupFullDetailsResponse;
import education.kub.backend.ce.domain.group.model.GroupShortDetailsResponse;
import education.kub.backend.ce.domain.group.model.GroupUpdateRequest;
import education.kub.backend.ce.domain.group.repository.GroupRepository;
import education.kub.backend.ce.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final StudentRepository studentRepository;

    public GroupShortDetailsResponse createGroup(GroupCreateRequest groupCreateRequest) {
        GroupEntity group = new GroupEntity();
        group.setName(groupCreateRequest.name());
        groupRepository.save(group);

        return groupMapper.toShortDetailsResponse(group);
    }

    public List<GroupShortDetailsResponse> getAllGroups() {
        var groups = groupRepository.findAll();

        return groupMapper.toShortDetailsResponseList(groups);
    }

    public GroupFullDetailsResponse getGroupById(Long id) {
        var group = groupRepository.findGroupWithStudentsById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return groupMapper.toFullDetailsResponse(group);
    }

    public GroupShortDetailsResponse updateGroup(Long id, GroupUpdateRequest groupUpdateRequest) {
        var group = groupRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        group.setName(groupUpdateRequest.name());

        groupRepository.save(group);

        return groupMapper.toShortDetailsResponse(group);
    }

    public void deleteGroup(Long id) {
        var group = groupRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        groupRepository.delete(group);
    }

    public GroupFullDetailsResponse addStudentToGroup(Long groupId, Long studentId) {
        var group = groupRepository.findGroupWithStudentsById(groupId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        group.addStudent(student);

        groupRepository.save(group);

        return groupMapper.toFullDetailsResponse(group);
    }

    public GroupFullDetailsResponse removeStudentFromGroup(Long groupId, Long studentId) {
        var group = groupRepository.findGroupWithStudentsById(groupId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        group.removeStudent(student);

        groupRepository.save(group);

        return groupMapper.toFullDetailsResponse(group);
    }
}
