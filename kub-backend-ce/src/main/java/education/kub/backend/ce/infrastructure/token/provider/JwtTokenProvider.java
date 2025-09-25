package education.kub.backend.ce.infrastructure.token.provider;

import education.kub.backend.ce.app.properties.AppSecurityProperties;
import education.kub.backend.ce.infrastructure.token.model.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String ROLES_CLAIM = "roles";
    private static final String USER_ID_CLAIM = "userId";
    private static final String SESSION_ID_CLAIM = "sessionId";

    private final AppSecurityProperties appSecurityProperties;

    public String generateAccessToken(Collection<String> roles, Long userId, String sessionId) {
        return generateToken(roles, userId, sessionId, appSecurityProperties.accessTokenValidityMs());
    }

    public String generateRefreshToken(Collection<String> roles, Long userId, String sessionId) {
        return generateToken(roles, userId, sessionId, appSecurityProperties.refreshTokenValidityMs());
    }

    public Claims parseToken(String token) {
        return parser().parseSignedClaims(token).getPayload();
    }

    public List<String> getRoles(String token) {
        return readStringListClaim(parseToken(token), ROLES_CLAIM);
    }

    public Long getUserId(String token) {
        final Claims claims = parseToken(token);
        Number n = claims.get(USER_ID_CLAIM, Number.class);

        return n == null ? null : n.longValue();
    }

    public String getSessionId(String token) {
        final Claims claims = parseToken(token);

        return claims.get(SESSION_ID_CLAIM, String.class);
    }

    public TokenDto parseAllClaims(String token) {
        Claims claims = parseToken(token);
        Number n = claims.get(USER_ID_CLAIM, Number.class);
        Long userId = n == null ? null : n.longValue();
        String sessionId = claims.get(SESSION_ID_CLAIM, String.class);
        List<String> roles = readStringListClaim(claims, ROLES_CLAIM);

        return new TokenDto(userId, sessionId, roles);
    }

    public Boolean isTokenExpired(String token) {
        Date exp = parser().parseSignedClaims(token).getPayload().getExpiration();

        return exp != null && exp.before(new Date());
    }

    private String generateToken(Collection<String> roles, Long userId, String sessionId, long validityMs) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roleNames = roles == null ? List.of()
                : roles.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        claims.put(ROLES_CLAIM, roleNames);
        claims.put(USER_ID_CLAIM, userId);
        claims.put(SESSION_ID_CLAIM, sessionId);

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(now))
                .expiration(new Date(now + validityMs))
                .signWith(signingKey())
                .compact();
    }

    private Key signingKey() {
        return Keys.hmacShaKeyFor(appSecurityProperties.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    private JwtParser parser() {
        return Jwts.parser()
                .verifyWith((SecretKey) signingKey())
                .build();
    }

    private static List<String> readStringListClaim(Claims claims, String claimName) {
        Object raw = claims.get(claimName);

        if (raw == null) {
            return List.of();
        }

        if (raw instanceof List<?> list) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        if (raw instanceof String s) {
            if (s.isBlank()) {
                return List.of();
            }

            return Arrays.stream(s.split(","))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .collect(Collectors.toList());
        }

        return List.of(raw.toString());
    }
}
