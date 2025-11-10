package education.kub.backend.ce.domain.student.model;

import education.kub.backend.ce.domain.user.model.UserDetailsResponse;

public record StudentShortDetailsResponse(
        Long id,

        UserDetailsResponse user

//        List<StudentEducationalProgramDetails> studentEducationalPrograms,

//        List<GroupDetails> groups
) {
}
