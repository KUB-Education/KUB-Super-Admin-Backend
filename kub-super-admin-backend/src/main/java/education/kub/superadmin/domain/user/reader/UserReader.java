package education.kub.superadmin.domain.user.reader;

import education.kub.superadmin.domain.user.entity.UserEntity;

public interface UserReader {
    UserEntity getUser(Long id);
}
