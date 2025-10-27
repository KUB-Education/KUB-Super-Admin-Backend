package education.kub.backend.ce.domain.academic_title.model;

import education.kub.backend.ce.domain.academic_title.entity.AcademicTitleEntity;

public record AcademicTitleDto(
        Long id,

        AcademicTitleEntity.AcademicTitleName name
) {
}
