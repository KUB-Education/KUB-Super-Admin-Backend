package education.kub.superadmin.app.exception.model;

import java.util.List;

public record ValidationExceptionErrorDTO(
        List<String> errors
) {}
