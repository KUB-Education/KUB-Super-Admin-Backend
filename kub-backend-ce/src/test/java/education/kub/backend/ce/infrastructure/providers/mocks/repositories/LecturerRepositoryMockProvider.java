package education.kub.backend.ce.infrastructure.providers.mocks.repositories;

import education.kub.backend.ce.domain.lecturer.entity.LecturerEntity;
import education.kub.backend.ce.domain.lecturer.repository.LecturerRepository;

import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

import java.util.*;

public class LecturerRepositoryMockProvider {
    private static long id_count = 0;

    public static void resetMocks() {
        id_count = 0;
    }

    private static void saveLecturer(LecturerRepository mock, LecturerEntity lecturer) {
        Mockito.lenient().doReturn(Optional.of(lecturer)).when(mock).findFullEntityById(lecturer.getId());
    }

    public static LecturerRepository createLecturerRepositoryMock() {
        LecturerRepository lecturerRepo = Mockito.mock(LecturerRepository.class);
        Mockito.lenient().doAnswer(invocation -> {
            var saved_lecturer = invocation.getArgument(0, LecturerEntity.class);
            saved_lecturer.setId(id_count++);
            saveLecturer(lecturerRepo, saved_lecturer);
            return saved_lecturer;
        }).when(lecturerRepo).save(any(LecturerEntity.class));
        return lecturerRepo;
    }
}
