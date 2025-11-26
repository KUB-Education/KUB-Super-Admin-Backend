package education.kub.backend.ce.infrastructure.properties.study_field;

import education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity;
import static education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity.DegreeType.BACHELOR;
import static education.kub.backend.ce.domain.educational_program.entity.EducationalProgramEntity.StudyForm.FULL_TIME;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
public class EducationalProgramProperties {
    @Builder.Default
    private Long id = 0L;
    @Builder.Default
    private Long specialty_id = 0L;
    @Builder.Default
    private String name = "Software Engineering";
    @Builder.Default
    private EducationalProgramEntity.DegreeType degree_type = BACHELOR;
    @Builder.Default
    private EducationalProgramEntity.StudyForm study_form = FULL_TIME;
    @Builder.Default
    private Short duration = 30;

    public EducationalProgramRequestUrlParameters toUrlParameters() {
        return EducationalProgramRequestUrlParameters.builder().program_id(id.toString()).build();
    }

    public Map<String, Object> toMap() {
        var map = new HashMap<String, Object>();
        if (specialty_id != null) {
            map.put("specialty_id", specialty_id);
        }
        if (name != null) {
            map.put("name", name);
        }
        if (degree_type != null) {
            map.put("degree_type", degree_type.toString());
        }
        if (study_form != null) {
            map.put("study_form", study_form.toString());
        }
        if (duration != null) {
            map.put("duration", duration);
        }
        return map;
    }
}
