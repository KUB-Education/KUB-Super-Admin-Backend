package education.kub.backend.ce.domain.educational_program.model;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;

public record EducationalProgramDetailsResponse(
        Long id,

        Long specialtyId,

        String name,

        EducationalProgramEntity.DegreeType degreeType,

        EducationalProgramEntity.StudyForm studyForm,

        Short duration
) {}
