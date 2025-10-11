package education.kub.backend.ce.domain.lecturer.model;

import jakarta.validation.constraints.NotNull;

public record LecturerAddAcademicTitleRequest(
        @NotNull(message = "academic title ID can't be null")
        Long academicTitleId
){
}
