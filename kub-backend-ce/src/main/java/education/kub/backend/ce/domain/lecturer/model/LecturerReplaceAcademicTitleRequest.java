package education.kub.backend.ce.domain.lecturer.model;

public record LecturerReplaceAcademicTitleRequest(
        Long oldAcademicTitleId,

        Long newAcademicTitleId
) {
}
