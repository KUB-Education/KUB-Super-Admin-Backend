package education.kub.backend.ce.domain.auth.controller;


import education.kub.backend.ce.app.exception.handler.GlobalExceptionHandler;
import education.kub.backend.ce.domain.auth.service.AuthService;
import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.domain.auth.controller.helpers.AuthProvider;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;

import education.kub.backend.ce.infrastructure.token.provider.JwtTokenProvider;
import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;

import io.restassured.path.json.JsonPath;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"education"})
@EnableJpaRepositories(basePackages={"education"})
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:test.application.properties"})
public class AuthControllerTests {

    @Value("${server.port}")
    private String port;
    @Value("${server.host}")
    private String host;
    @Value("${server.protocol}")
    private String protocol;
    private String backend_url;

    private final String last_name = "Doe";
    private final String first_name = "John";
    private final String middle_name = "Edward";
    private final String email = "allex.nevedrov@example.com";
    private final String password = "~NewPass456";

    private final long user_id = 0;

    private String accessToken;
    private String refreshToken;

    private MockMvc mockMvc;
    @Autowired
    private TokenStoreService tokenStoreService;
    @Spy
    private UserRepository userRepo;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void initializeMocks() {
        backend_url = String.format("%s://%s:%s", protocol, host, port);
        Mockito.doAnswer(invocation -> {
            if (userRepo.findById(user_id).isPresent()) {
                return Optional.of(userRepo.findById(user_id).get());
            }
            else {
                UserEntity user = new UserEntity();
                user.setId(user_id);
                user.setLastName(last_name);
                user.setFirstName(first_name);
                user.setMiddleName(middle_name);
                user.setEmail(email);
                user.setPasswordHashed(passwordService.hash(password));
                user.setStatus(UserEntity.Status.ACTIVATED);
                user.setTemporaryPasswordHashed(null);
                user.setTemporaryPasswordExpiresAt(null);
                user.setCreatedAt(null);
                user.setUpdatedAt(null);
                user.setDeletedAt(null);
                var userSet = new HashSet<UserEntity>();
                userSet.add(user);
                RoleEntity role = new RoleEntity(user_id, RoleEntity.Type.ADMIN, userSet);
                var roles = new HashSet<RoleEntity>();
                roles.add(role);
                userRepo.save(user);
                return Optional.of(user);
            }
        }).when(userRepo).findWithRolesByEmailAndDeletedAtIsNull(email);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(
                new AuthService(userRepo, passwordService,
                        tokenStoreService, jwtTokenProvider))).setControllerAdvice(globalExceptionHandler).build();
    }

    public String ValidateLogin(Map<String, Object> request_map, String bearer_token, int expectedStatusCode) {
        return AuthProvider.Login(mockMvc, backend_url, request_map, bearer_token,
                expectedStatusCode, "schemas/LoginResponse.json");
    }

    public void FirstLogin() {
        String response = ValidateLogin(Map.of("email", email, "password", password),
                null,200);

        JsonPath jsonPath = JsonPath.with(response);
        boolean is_first_login = jsonPath.get("first_login");
        assertFalse(is_first_login, "First login field must be set to false at not first login attempt");

        accessToken = jsonPath.get("access_token");
        refreshToken = jsonPath.get("refresh_token");
    }

    public void ValidateLogout(int expectedStatusCode) {
        AuthProvider.Logout(mockMvc, backend_url, accessToken, expectedStatusCode, true);
    }

    public void Logout() {
        ValidateLogout(204);
    }

    public void ValidateRefresh(Map<String, Object> request_body, int expectedStatusCode) {
        AuthProvider.Refresh(mockMvc, backend_url, request_body, expectedStatusCode,
                "schemas/RefreshResponse.json");
    }

    public void Refresh(int expectedStatusCode) {
        AuthProvider.Refresh(mockMvc, backend_url, Map.of("refresh_token", refreshToken), expectedStatusCode,
                "schemas/RefreshResponse.json");
    }

    public void Refresh() {
        Refresh(200);
    }

    @Nested
    public class LoginTests {

        @Test
        public void TestLoginSuccess() {
            AuthControllerTests.this.FirstLogin();
        }

        @Test
        public void TestLoginWithInvalidToken() {
            ValidateLogin(Map.of("email", email, "password", password),
                    "afdfasfda", 200);
        }

        @Test
        public void TestLoginWithInvalidPassword() {
            ValidateLogin(Map.of("email", email, "password", "dagagdg"),
                    "afdfasfda", 401);
        }

        @Test
        public void TestLoginWithWrongEmail() {
            ValidateLogin(Map.of("email", email, "password", "dagagdg"),
                    accessToken, 401);
        }

        @Test
        public void TestLoginWithInvalidBody() {
            ValidateLogin(Map.of("afafsfs", email, "fdfdf", password),
                    accessToken, 401);
        }

        @Test
        public void TestLogoutWithoutBody() {
            ValidateLogin(null, null, 400);
        }
    }

    @Nested
    public class LogoutTests {

        @Test
        public void TestLogoutSuccess() {
            FirstLogin();
            Logout();
        }

        @Test
        public void TestLogoutWithoutBearerToken() {
            AuthProvider.Logout(mockMvc, backend_url, null, 401, true);
        }

        @Test
        public void TestLogoutWithoutAuthorizationHeader() {
            AuthProvider.Logout(mockMvc, backend_url, null, 400, false);
        }

        @Test
        public void TestLogoutWithInvalidToken() {
            accessToken = "adsgadgdg";
            ValidateLogout(401);
        }

        @Test
        public void TestLogoutWithoutLogin() {
            ValidateLogout(401);
        }
    }

    @Nested
    public class RefreshTests {

        @Test
        public void TestRefreshSuccess() {
            FirstLogin();
            Refresh();
        }

        @Test
        public void TestWithoutBody() {
            FirstLogin();
            ValidateRefresh(null, 400);
        }

        @Test
        public void TestWithInvalidRefreshToken() {
            FirstLogin();
            refreshToken = "adsgadgdg";
            Refresh(401);
        }
    }
}
