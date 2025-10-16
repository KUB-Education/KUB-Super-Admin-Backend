package education.kub.backend.ce.domain.lecturer.model;

import jakarta.validation.constraints.NotNull;

public record LecturerUpdateAcademicTitleRequest(
        @NotNull(message = "new academic title ID can't be null")
        Long newAcademicTitleId
) {
}
