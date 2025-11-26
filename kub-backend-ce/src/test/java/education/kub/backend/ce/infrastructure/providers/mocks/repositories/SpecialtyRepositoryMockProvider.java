package education.kub.backend.ce.infrastructure.providers.mocks.repositories;

import education.kub.backend.ce.domain.specialty.entity.SpecialtyEntity;
import education.kub.backend.ce.domain.specialty.repository.SpecialtyRepository;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

public class SpecialtyRepositoryMockProvider {
    private static Map<Long, SpecialtyEntity> specialties = new HashMap<>();
    private static long id_count = 0;

    public static void resetMocks() {
        specialties.clear();
        id_count = 0;
    }

    private static void saveSpecialty(SpecialtyRepository mock, SpecialtyEntity specialty) {
        Mockito.lenient().doReturn(Optional.of(specialty)).when(mock).findById(specialty.getId());
        Mockito.lenient().doReturn(new ArrayList<SpecialtyEntity>(specialties.values())).when(mock).findAll();
        Mockito.lenient().doReturn(true).when(mock).existsById(specialty.getId());
        Mockito.lenient().doReturn(true).when(mock).existsByCode(specialty.getCode());
        Mockito.lenient().doAnswer( invocation -> {
            var code = invocation.getArgument(0, String.class);
            var id = invocation.getArgument(1, Long.class);
            var conflicts = specialties.entrySet().stream().
                    filter(entry -> {
                        var value = entry.getValue();
                        return !value.getId().equals(id) && value.getCode().equals(code);
                    }).count();
            if (conflicts == 0) {
                return false;
            }
            return true;
        }).when(mock).existsByCodeAndIdIsNot(specialty.getCode(), specialty.getId());
        Mockito.lenient().doAnswer(invocation ->  {
            specialties.remove(invocation.getArgument(0, Long.class));
            return null;
        }).when(mock).deleteById(specialty.getId());
    }

    public static SpecialtyRepository createSpecialtyRepositoryMock() {
        SpecialtyRepository SpecialtyRepo = Mockito.mock(SpecialtyRepository.class);
        Mockito.lenient().doAnswer(invocation -> {
            var saved_specialty = invocation.getArgument(0, SpecialtyEntity.class);
            saved_specialty.setId(id_count++);
            specialties.put(saved_specialty.getId(), saved_specialty);
            saveSpecialty(SpecialtyRepo, saved_specialty);
            return saved_specialty;
        }).when(SpecialtyRepo).save(any(SpecialtyEntity.class));
        return SpecialtyRepo;
    }
}
