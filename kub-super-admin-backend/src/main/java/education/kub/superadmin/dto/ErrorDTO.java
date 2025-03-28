package education.kub.superadmin.dto;

import java.util.List;

public record ErrorDTO(
        List<String> errors
) {}
