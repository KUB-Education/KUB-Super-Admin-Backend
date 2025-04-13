package education.kub.superadmin.domain.user.reader;

import education.kub.superadmin.app.exception.model.KubException;
import education.kub.superadmin.domain.user.entity.UserEntity;
import education.kub.superadmin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {
    private final UserRepository userRepository;

    @Override
    public UserEntity getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new KubException(KubException.ErrorCode.NOT_FOUND));
    }
}
