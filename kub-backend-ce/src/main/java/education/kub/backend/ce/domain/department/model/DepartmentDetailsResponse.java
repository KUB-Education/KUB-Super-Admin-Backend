package education.kub.backend.ce.domain.department.model;

public record DepartmentDetailsResponse(
        Long id,

        String name

        // Set<LecturerDepartmentPosition> could be added here
) {}