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

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableMapper timetableMapper;

    private final TimetableRepository timetableRepository;

    private final GroupRepository groupRepository;

    public TimetableDetailsResponse createTimetable(TimetableCreateRequest TimetableCreateRequest) {
        var timetable = new TimetableEntity();
        timetable.setName(TimetableCreateRequest.name());
        timetable.setTimeStart(TimetableCreateRequest.timeStart());
        timetable.setTimeEnd(TimetableCreateRequest.timeEnd());

        timetableRepository.save(timetable);

        return timetableMapper.toDetailsResponse(timetable);
    }

    public List<TimetableDetailsResponse> getAllTimetables() {
        var Specialties = timetableRepository.findAll();

        return timetableMapper.toDetailsResponseList(Specialties);
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
        if(timetableUpdateRequest.group() != null){
            var groupId = groupRepository.findById(timetableUpdateRequest.group())
                    .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
            timetable.setGroup(groupId);
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