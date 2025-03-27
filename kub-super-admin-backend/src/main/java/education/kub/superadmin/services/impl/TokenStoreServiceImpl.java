package education.kub.superadmin.services.impl;

import education.kub.superadmin.services.inter.TokenStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenStoreServiceImpl implements TokenStoreService {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenValidityMs;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenValidityMs;

    @Override
    public void storeSessionTokens(Long userId, String sessionId, String accessToken, String refreshToken) {
        String accessKey = getAccessTokenKey(userId, sessionId);
        redisTemplate.opsForValue().set(accessKey, accessToken, Duration.ofMillis(accessTokenValidityMs));

        String refreshKey = getRefreshTokenKey(userId, sessionId);
        redisTemplate.opsForValue().set(refreshKey, refreshToken, Duration.ofMillis(refreshTokenValidityMs));

        String sessionSetKey = getSessionSetKey(userId);
        redisTemplate.opsForSet().add(sessionSetKey, accessToken);
    }

    @Override
    public boolean isSessionValid(Long userId, String sessionId) {
        String accessKey = getAccessTokenKey(userId, sessionId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(accessKey));
    }

    @Override
    public boolean isRefreshTokenValid(Long userId, String sessionId) {
        String refreshKey = getRefreshTokenKey(userId, sessionId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(refreshKey));
    }

    @Override
    public void deleteSession(Long userId, String sessionId) {
        redisTemplate.delete(getAccessTokenKey(userId, sessionId));
        redisTemplate.delete(getRefreshTokenKey(userId, sessionId));
        redisTemplate.opsForSet().remove(getSessionSetKey(userId), sessionId);
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

    private String getAccessTokenKey(Long userId, String sessionId) {
        return "session:" + userId + ":" + sessionId;
    }

    private String getRefreshTokenKey(Long userId, String sessionId) {
        return "refresh:" + userId + ":" + sessionId;
    }

    private String getSessionSetKey(Long userId) {
        return "sessions:" + userId;
    }
}
