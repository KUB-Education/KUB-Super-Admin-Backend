package education.kub.backend.ce.infrastructure.token.store.service;

public interface TokenStoreService {
    void deleteAllSessions(Long userId);
}
