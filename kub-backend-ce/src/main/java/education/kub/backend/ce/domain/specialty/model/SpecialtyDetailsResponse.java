package education.kub.backend.ce.domain.specialty.model;


public record SpecialtyDetailsResponse (
        Long id,

        Long studyFieldId,

        String code,

        String name
) {}