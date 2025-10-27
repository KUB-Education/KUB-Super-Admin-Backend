package education.kub.backend.ce.helpers.providers.mocks.services;


import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;

public class TokenStoreServiceMockProvider {

    public static TokenStoreService createTokenStoreServiceMock() {
        return new TokenStoreServiceMock();
    }

    public static class TokenStoreServiceMock implements TokenStoreService {
        private String accessToken;
        private String refreshToken;

        public void storeSessionTokens(Long userId, String sessionId, String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public boolean isAccessTokenValid(Long userId, String sessionId, String accessToken) {
            return this.accessToken.equals(accessToken);
        }

        public boolean isRefreshTokenValid(Long userId, String sessionId, String refreshToken) {
            return this.refreshToken.equals(refreshToken);
        }

        public void deleteSession(Long userId, String sessionId) {
            accessToken = null;
            refreshToken = null;
        }

        public void deleteAllSessions(Long userId) {
            accessToken = null;
            refreshToken = null;
        }
    }
}
