package education.kub.backend.ce.infrastructure.token.store.service;

public interface TokenStoreService {
    void storeSessionTokens(Long userId, String sessionId, String accessToken, String refreshToken);

    boolean isAccessTokenValid(Long userId, String sessionId, String accessToken);

    boolean isRefreshTokenValid(Long userId, String sessionId, String refreshToken);

    void deleteSession(Long userId, String sessionId);

    void deleteAllSessions(Long userId);
}
