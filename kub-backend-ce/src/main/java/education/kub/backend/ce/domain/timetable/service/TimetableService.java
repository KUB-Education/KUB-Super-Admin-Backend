package education.kub.backend.ce.domain.timetable.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.group.repository.GroupRepository;
import education.kub.backend.ce.domain.timetable.entity.TimetableEntity;
import education.kub.backend.ce.domain.timetable.mapper.TimetableMapper;
import education.kub.backend.ce.domain.timetable.model.TimetableCreateRequest;
import education.kub.backend.ce.domain.timetable.model.TimetableDetailsResponse;
import education.kub.backend.ce.domain.timetable.model.TimetableUpdateRequest;
import education.kub.backend.ce.domain.timetable.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static education.kub.backend.ce.domain.timetable.entity.TimetableEntity.Status.DRAFT;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableMapper timetableMapper;

    private final TimetableRepository timetableRepository;

    private final GroupRepository groupRepository;

    public TimetableDetailsResponse createTimetable(TimetableCreateRequest timetableCreateRequest) {
        var timetable = new TimetableEntity();
        timetable.setName(timetableCreateRequest.name());
        timetable.setTimeStart(timetableCreateRequest.timeStart());
        timetable.setTimeEnd(timetableCreateRequest.timeEnd());
        var group = groupRepository.findById(timetableCreateRequest.groupId())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        timetable.setGroup(group);
        timetable.setStatus(DRAFT);
        timetableRepository.save(timetable);

        return timetableMapper.toDetailsResponse(timetable);
    }

    public List<TimetableDetailsResponse> getAllTimetables() {
        var timetables = timetableRepository.findAll();

        return timetableMapper.toDetailsResponseList(timetables);
    }

    public TimetableDetailsResponse getTimetableById(Long id) {
        var timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return timetableMapper.toDetailsResponse(timetable);
    }

    public TimetableDetailsResponse updateTimetable(Long id, TimetableUpdateRequest timetableUpdateRequest) {
        var timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
        if(timetableUpdateRequest.name() != null){
            timetable.setName(timetableUpdateRequest.name());
        }
        if(timetableUpdateRequest.timeStart() != null){
            timetable.setTimeStart(timetableUpdateRequest.timeStart());
        }
        if(timetableUpdateRequest.timeEnd() != null){
            timetable.setTimeEnd(timetableUpdateRequest.timeEnd());
        }
        if(timetableUpdateRequest.groupId() != null){
            var group = groupRepository.findById(timetableUpdateRequest.groupId())
                    .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
            timetable.setGroup(group);
        }
        if(timetableUpdateRequest.status() != null){
            timetable.setStatus(timetableUpdateRequest.status());
        }
        timetableRepository.save(timetable);

        return timetableMapper.toDetailsResponse(timetable);
    }

    public void deleteTimetable(Long id) {
        if (!timetableRepository.existsById(id)) {
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        timetableRepository.deleteById(id);
    }
}