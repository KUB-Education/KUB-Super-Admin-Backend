package education.kub.backend.ce.domain.term.model;

public record TermDetailsResponse(
        Long id,
        Long educationalProgramId,
        Short number
) {}
