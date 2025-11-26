package education.kub.backend.ce.infrastructure.providers.mocks.repositories;

import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;
import education.kub.backend.ce.domain.study_field.repository.StudyFieldRepository;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

public class StudyFieldRepositoryMockProvider {
    private static Map<Long, StudyFieldEntity> study_fields = new HashMap<>();
    private static long id_count = 0;

    public static void resetMocks() {
        study_fields.clear();
        id_count = 0;
    }

    private static void saveStudyField(StudyFieldRepository mock, StudyFieldEntity study_field) {
        Mockito.lenient().doReturn(Optional.of(study_field)).when(mock).findById(study_field.getId());
        Mockito.lenient().doReturn(true).when(mock).existsById(study_field.getId());
        Mockito.lenient().doReturn(new ArrayList<StudyFieldEntity>(study_fields.values())).when(mock).findAll();
        Mockito.lenient().doReturn(true).when(mock).existsByCode(study_field.getCode());
        Mockito.lenient().doAnswer( invocation -> {
            var code = invocation.getArgument(0, String.class);
            var id = invocation.getArgument(1, Long.class);
            var conflicts = study_fields.entrySet().stream().
                    filter(entry -> {
                        var value = entry.getValue();
                        return !value.getId().equals(id) && value.getCode().equals(code);
                    }).count();
            if (conflicts == 0) {
                return false;
            }
            return true;
        }).when(mock).existsByCodeAndIdIsNot(study_field.getCode(), study_field.getId());
        Mockito.lenient().doAnswer(invocation ->  {
            study_fields.remove(invocation.getArgument(0, Long.class));
            return null;
        }).when(mock).deleteById(study_field.getId());
    }

    public static StudyFieldRepository createStudyFieldRepositoryMock() {
        StudyFieldRepository StudyFieldRepo = Mockito.mock(StudyFieldRepository.class);
        Mockito.lenient().doAnswer(invocation -> {
            var saved_study_field = invocation.getArgument(0, StudyFieldEntity.class);
            saved_study_field.setId(id_count++);
            study_fields.put(saved_study_field.getId(), saved_study_field);
            saveStudyField(StudyFieldRepo, saved_study_field);
            return saved_study_field;
        }).when(StudyFieldRepo).save(any(StudyFieldEntity.class));
        return StudyFieldRepo;
    }
}
