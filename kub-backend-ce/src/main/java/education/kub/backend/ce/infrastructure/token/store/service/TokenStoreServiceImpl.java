package education.kub.backend.ce.infrastructure.token.store.service;

import education.kub.backend.ce.app.properties.AppSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenStoreServiceImpl implements TokenStoreService {
    private final RedisTemplate<String, String> redisTemplate;

    private final AppSecurityProperties appSecurityProperties;

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
    public void storeSessionTokens(Long userId, String sessionId, String accessToken, String refreshToken) {
        String accessKey = getAccessTokenKey(userId, sessionId);
        redisTemplate.opsForValue().set(accessKey, accessToken, Duration.ofMillis(appSecurityProperties.accessTokenValidityMs()));

        String refreshKey = getRefreshTokenKey(userId, sessionId);
        redisTemplate.opsForValue().set(refreshKey, refreshToken, Duration.ofMillis(appSecurityProperties.refreshTokenValidityMs()));

        String sessionSetKey = getSessionSetKey(userId);
        redisTemplate.opsForSet().add(sessionSetKey, sessionId);
    }

    @Override
    public boolean isAccessTokenValid(Long userId, String sessionId, String accessToken) {
        String accessKey = getAccessTokenKey(userId, sessionId);
        String storedAccessToken = redisTemplate.opsForValue().get(accessKey);

        return accessToken.equals(storedAccessToken);
    }

    @Override
    public boolean isRefreshTokenValid(Long userId, String sessionId, String refreshToken) {
        String refreshKey = getRefreshTokenKey(userId, sessionId);
        String storedRefreshToken = redisTemplate.opsForValue().get(refreshKey);

        return refreshToken.equals(storedRefreshToken);
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
}
