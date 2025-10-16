package education.kub.backend.ce.domain.specialty.model;

import education.kub.backend.ce.domain.study_field.domain.StudyFieldEntity;

public record SpecialtyDetailsResponse (
        Long id,

        StudyFieldEntity studyField,

        String code,

        String name
) {}