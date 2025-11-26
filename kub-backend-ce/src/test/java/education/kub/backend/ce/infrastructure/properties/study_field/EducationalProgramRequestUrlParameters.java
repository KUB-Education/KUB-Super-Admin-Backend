package education.kub.backend.ce.infrastructure.properties.study_field;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EducationalProgramRequestUrlParameters {
    @Builder.Default
    public String program_id = "0";

    public String toUrlRequestParametersSubstring() {
        return program_id;
    }
}
