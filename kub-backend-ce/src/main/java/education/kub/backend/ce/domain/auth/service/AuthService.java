package education.kub.backend.ce.domain.auth.service;

import education.kub.backend.ce.app.exception.model.KubException;
import education.kub.backend.ce.domain.auth.model.LoginRequest;
import education.kub.backend.ce.domain.auth.model.LoginResponse;
import education.kub.backend.ce.domain.auth.model.RefreshRequest;
import education.kub.backend.ce.domain.auth.model.RefreshResponse;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.infrastructure.token.model.TokenDto;
import education.kub.backend.ce.infrastructure.token.provider.JwtTokenProvider;
import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordService passwordService;

    private final TokenStoreService tokenStoreService;

    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest loginRequest) {
        var user = userRepository.findWithRolesByEmailAndDeletedAtIsNull(loginRequest.email())
                .orElseThrow(() -> new KubException(KubException.ErrorCode.UNAUTHORIZED));

        boolean firstLogin = false;
        String passwordHashed;
        List<String> roles;

        if (user.getTemporaryPasswordHashed() != null) {
            passwordHashed = user.getTemporaryPasswordHashed();
            firstLogin = true;
        } else if (user.getPasswordHashed() != null) {
            passwordHashed = user.getPasswordHashed();
        } else {
            throw new KubException(KubException.ErrorCode.UNAUTHORIZED);
        }

        if (!passwordService.matches(loginRequest.password(), passwordHashed)) {
            throw new KubException(KubException.ErrorCode.UNAUTHORIZED);
        }

        if (firstLogin) {
            String newPasswordHashed = passwordService.hash(loginRequest.password());
            user.setTemporaryPasswordHashed(null);
            user.setTemporaryPasswordExpiresAt(null);
            user.setPasswordHashed(newPasswordHashed);
            user.setStatus(UserEntity.Status.ACTIVATED);
            userRepository.save(user);

            tokenStoreService.deleteAllSessions(user.getId());
        }

        roles = user.getRoles().stream().map(RoleEntity::getType).map(Enum::toString).toList();

        String sessionId = UUID.randomUUID().toString();
        String accessToken = jwtTokenProvider.generateAccessToken(roles, user.getId(), sessionId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(roles, user.getId(), sessionId);

        tokenStoreService.storeSessionTokens(user.getId(), sessionId, accessToken, refreshToken);

        return new LoginResponse(accessToken, refreshToken, firstLogin);
    }

    public void logout(String token) {
        Long userId = jwtTokenProvider.getUserId(token);

        String sessionId = jwtTokenProvider.getSessionId(token);

        tokenStoreService.deleteSession(userId, sessionId);
    }

    public RefreshResponse refresh(RefreshRequest refreshRequest) {
        TokenDto token = jwtTokenProvider.parseAllClaims(refreshRequest.refreshToken());

        if (!tokenStoreService.isRefreshTokenValid(token.userId(), token.sessionId(), refreshRequest.refreshToken())) {
            throw new KubException(KubException.ErrorCode.UNAUTHORIZED);
        }

        tokenStoreService.deleteSession(token.userId(), token.sessionId());

        String newSessionId = UUID.randomUUID().toString();
        String newAccessToken = jwtTokenProvider.generateAccessToken(token.roles(), token.userId(), newSessionId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(token.roles(), token.userId(), newSessionId);

        tokenStoreService.storeSessionTokens(token.userId(), newSessionId, newAccessToken, newRefreshToken);

        return new RefreshResponse(newAccessToken, newRefreshToken);
    }
}
