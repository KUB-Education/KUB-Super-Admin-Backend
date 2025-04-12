package education.kub.superadmin.infrastructure.token.store.service;

public interface TokenStoreService {
    void deleteAllSessions(Long userId);
}
