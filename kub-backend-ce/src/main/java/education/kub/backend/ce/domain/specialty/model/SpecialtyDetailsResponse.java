package education.kub.backend.ce.domain.specialty.model;

import education.kub.backend.ce.domain.study_field.entity.StudyFieldEntity;

public record SpecialtyDetailsResponse (
        Long id,

        Long studyFieldId,

        String code,

        String name
) {}