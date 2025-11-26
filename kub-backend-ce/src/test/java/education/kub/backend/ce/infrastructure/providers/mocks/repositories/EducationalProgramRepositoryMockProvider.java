package education.kub.backend.ce.infrastructure.providers.mocks.repositories;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import education.kub.backend.ce.domain.educational_program.repository.EducationalProgramRepository;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

public class EducationalProgramRepositoryMockProvider {
    private static Map<Long, EducationalProgramEntity> programs = new HashMap<>();
    private static long id_count = 0;

    public static void resetMocks() {
        programs.clear();
        id_count = 0;
    }

    private static void saveEducationalProgram(EducationalProgramRepository mock, EducationalProgramEntity program) {
        Mockito.lenient().doReturn(Optional.of(program)).when(mock).findById(program.getId());
        Mockito.lenient().doReturn(true).when(mock).existsById(program.getId());
        Mockito.lenient().doReturn(new ArrayList<EducationalProgramEntity>(programs.values())).when(mock).findAll();
        Mockito.lenient().doReturn(true).when(mock).existsByName(program.getName());
        Mockito.lenient().doAnswer( invocation -> {
            var name = invocation.getArgument(0, String.class);
            var id = invocation.getArgument(1, Long.class);
            var conflicts = programs.entrySet().stream().
                    filter(entry -> {
                        var value = entry.getValue();
                        return !value.getId().equals(id) && value.getName().equals(name);
                    }).count();
            if (conflicts == 0) {
                return false;
            }
            return true;
        }).when(mock).existsByNameAndIdIsNot(program.getName(), program.getId());
        Mockito.lenient().doAnswer(invocation ->  {
            programs.remove(invocation.getArgument(0, Long.class));
            return null;
        }).when(mock).deleteById(program.getId());
    }

    public static EducationalProgramRepository createEducationalProgramRepositoryMock() {
        EducationalProgramRepository EducationalProgramRepo = Mockito.mock(EducationalProgramRepository.class);
        Mockito.lenient().doAnswer(invocation -> {
            var saved_educational_program = invocation.getArgument(0, EducationalProgramEntity.class);
            saved_educational_program.setId(id_count++);
            programs.put(saved_educational_program.getId(), saved_educational_program);
            saveEducationalProgram(EducationalProgramRepo, saved_educational_program);
            return saved_educational_program;
        }).when(EducationalProgramRepo).save(any(EducationalProgramEntity.class));
        return EducationalProgramRepo;
    }
}
