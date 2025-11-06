package education.kub.backend.ce.domain.student.controller;

import education.kub.backend.ce.domain.lecturer.model.LecturerDetailsResponse;
import education.kub.backend.ce.domain.student.mapper.StudentMapper;
import education.kub.backend.ce.domain.student.model.StudentCreateRequest;
import education.kub.backend.ce.domain.student.model.StudentShortDetailsResponse;
import education.kub.backend.ce.domain.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    final private StudentService studentService;

    final private StudentMapper studentMapper;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<StudentShortDetailsResponse> createStudent(
            @Valid @RequestBody StudentCreateRequest studentCreateRequest
    ) {
        var student = studentService.createStudent(studentCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

}
