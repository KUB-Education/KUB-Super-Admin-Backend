package education.kub.backend.ce.infrastructure.providers.mocks.repositories;

import education.kub.backend.ce.domain.student.entity.StudentEntity;
import education.kub.backend.ce.domain.student.repository.StudentRepository;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

public class StudentRepositoryMockProvider {
    private static long id_count = 0;

    public static void resetMocks() {
        id_count = 0;
    }

    private static void saveStudent(StudentRepository mock, StudentEntity lecturer) {
        Mockito.lenient().doReturn(Optional.of(lecturer)).when(mock).findById(lecturer.getId());
    }

    public static StudentRepository createStudentRepositoryMock() {
        StudentRepository studentRepo = Mockito.mock(StudentRepository.class);
        Mockito.lenient().doAnswer(invocation -> {
            var saved_student = invocation.getArgument(0, StudentEntity.class);
            saved_student.setId(id_count++);
            saveStudent(studentRepo, saved_student);
            return saved_student;
        }).when(studentRepo).save(any(StudentEntity.class));
        return studentRepo;
    }
}
