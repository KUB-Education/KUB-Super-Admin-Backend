package education.kub.backend.ce.domain.timetable.service;

import education.kub.backend.ce.app.exception.model.KubException;
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

    public TimetableDetailsResponse createTimetable(TimetableCreateRequest TimetableCreateRequest) {
        if (timetableRepository.existsByName(TimetableCreateRequest.name())) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        var Timetable = new TimetableEntity();
        Timetable.setName(TimetableCreateRequest.name());
        Timetable.setTimeStart(TimetableCreateRequest.timeStart());
        Timetable.setTimeEnd(TimetableCreateRequest.timeEnd());

        timetableRepository.save(Timetable);

        return timetableMapper.toDetailsResponse(Timetable);
    }

    public List<TimetableDetailsResponse> getAllSpecialties() {
        var Specialties = timetableRepository.findAll();

        return timetableMapper.toDetailsResponseList(Specialties);
    }

    public TimetableDetailsResponse getTimetableById(Long id) {
        var Timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        return timetableMapper.toDetailsResponse(Timetable);
    }

    public TimetableDetailsResponse updateTimetable(Long id, TimetableUpdateRequest timetableUpdateRequest) {
        if (timetableRepository.existsByName(timetableUpdateRequest.name())) {
            throw new KubException(KubException.ErrorCode.CONFLICT);
        }

        var Timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));

        Timetable.setName(timetableUpdateRequest.name());
        Timetable.setTimeStart(timetableUpdateRequest.timeStart());
        Timetable.setTimeEnd(timetableUpdateRequest.timeEnd());
        timetableRepository.save(Timetable);

        return timetableMapper.toDetailsResponse(Timetable);
    }

    public void deleteTimetable(Long id) {
        if (!timetableRepository.existsById(id)) {
            throw new KubException(KubException.ErrorCode.NOT_FOUND);
        }

        timetableRepository.deleteById(id);
    }
}