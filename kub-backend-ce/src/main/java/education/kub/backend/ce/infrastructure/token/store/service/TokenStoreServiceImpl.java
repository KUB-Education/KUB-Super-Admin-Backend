package education.kub.backend.ce.infrastructure.token.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenStoreServiceImpl implements TokenStoreService {
    private final RedisTemplate<String, String> redisTemplate;

    private String getAccessTokenKey(Long userId, String sessionId) {
        return "access:" + userId + ":" + sessionId;
    }

    private String getRefreshTokenKey(Long userId, String sessionId) {
        return "refresh:" + userId + ":" + sessionId;
    }

    private String getSessionSetKey(Long userId) {
        return "sessions:" + userId;
    }

    @Override
    public void deleteAllSessions(Long userId) {
        String sessionSetKey = getSessionSetKey(userId);
        Set<String> sessionIds = redisTemplate.opsForSet().members(sessionSetKey);

        if (sessionIds != null) {
            for (String sessionId : sessionIds) {
                redisTemplate.delete(getAccessTokenKey(userId, sessionId));
                redisTemplate.delete(getRefreshTokenKey(userId, sessionId));
            }
            redisTemplate.delete(sessionSetKey);
        }
    }
}
