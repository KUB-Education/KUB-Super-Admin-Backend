package education.kub.backend.ce.app.exception.model;

import java.util.List;

public record ValidationExceptionErrorDTO(
        List<String> errors
) {}
