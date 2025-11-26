package education.kub.backend.ce.infrastructure.properties.study_field;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SpecialtyProperties {
    @Builder.Default
    private Long id = 0L;
    @Builder.Default
    private Long study_field_id = 0L;
    @Builder.Default
    private String code = "121";
    @Builder.Default
    private String name = "Software Engineering";
}
