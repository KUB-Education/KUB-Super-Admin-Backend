package education.kub.superadmin.services.inter;

public interface TokenStoreService {
    void storeSessionTokens(Long userId, String sessionId, String accessToken, String refreshToken);

    boolean isSessionValid(Long userId, String sessionId);

    boolean isRefreshTokenValid(Long userId, String sessionId);

    void deleteSession(Long userId, String sessionId);

    void deleteAllSessions(Long userId);
}
