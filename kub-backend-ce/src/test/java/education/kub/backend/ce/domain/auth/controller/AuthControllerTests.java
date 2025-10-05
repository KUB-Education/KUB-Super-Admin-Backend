package education.kub.backend.ce.domain.auth.controller;


import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.domain.user.repository.UserRepository;
import education.kub.backend.ce.domain.auth.controller.helpers.AuthProvider;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.user.entity.UserEntity;

import io.restassured.path.json.JsonPath;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@WebMvcTest(AuthController.class)
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"education.kub.backend.ce.domain.auth",
                                "education.kub.backend.ce.domain.user.repository",
                                "education.kub.backend.ce.infrastructure.token",
                                "education.kub.backend.ce.infrastructure.password"})
@EnableJpaRepositories(basePackages="education.kub.backend.ce.domain.user.repository")
public class AuthControllerTests {

    private File mails_catcher_dir = new File("D:/SMTPService/mails");

    private String backend_url = "http://localhost:10000";

    private String superadmin_url = "http://localhost:10001";
    private String superadmin_key = "SuperToken";

    private String admin_last_name = "Doe";
    private String admin_first_name = "John";
    private String admin_middle_name = "Edward";
    private String admin_email = "allex.nevedrov@example.com";
    private String admin_password = "~NewPass456";

    private long admin_id = 1;
    private long user_id = 1;

    private String accessToken;
    private String refreshToken;
    private boolean first_login = true;

    @Autowired
    private MockMvc mockMvc;
    @Spy
    private UserRepository userRepo;
    @Autowired
    private PasswordService passwordService;

    @BeforeEach
    public void initializeMocks() {
        Mockito.doAnswer(invocation -> {
            if (userRepo.findById(user_id).isPresent()) {
                return userRepo.findById(user_id).get();
            }
            else {
                UserEntity user = new UserEntity();
                user.setId(admin_id);
                user.setLastName(admin_last_name);
                user.setFirstName(admin_first_name);
                user.setMiddleName(admin_middle_name);
                user.setEmail(admin_email);
                user.setPasswordHashed(passwordService.hash(admin_password));
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
                return user;
            }
        }).when(userRepo).findWithRolesByEmailAndDeletedAtIsNull(admin_email);//when(authService).login(any(LoginRequest.class));
        //mockMvc = MockMvcBuilders.standaloneSetup(AuthController.class).build();
    }

    public String ValidateLogin(Map<String, Object> request_map, String bearer_token, int expectedStatusCode) {
        return AuthProvider.Login(mockMvc, backend_url, request_map, bearer_token,
                expectedStatusCode, "schemas/LoginResponse.json");
    }

    public void FirstLogin() {
        String response = ValidateLogin(Map.of("email", admin_email, "password", admin_password),
                null,200);

        boolean is_first_login = JsonPath.with(response).get("first_login");
        assertFalse(is_first_login, "First login field must be set to false at not first login attempt");

        accessToken = JsonPath.with(response).get("access_token");
        refreshToken = JsonPath.with(response).get("refresh_token");
        first_login = is_first_login;
    }

    public void ValidateLogout(int expectedStatusCode) {
        AuthProvider.Logout(mockMvc, backend_url, accessToken,
                204);
    }

    public void Logout() {
        ValidateLogout(204);
    }

    public void ValidateRefresh(Map<String, Object> request_body, int expectedStatusCode) {
        AuthProvider.Refresh(mockMvc, request_body, backend_url, expectedStatusCode,
                "schemas/RefreshResponse.json");
    }

    public void Refresh(int expectedStatusCode) {
        AuthProvider.Refresh(mockMvc, Map.of("refresh_token", refreshToken), backend_url, expectedStatusCode,
                "schemas/RefreshResponse.json");
    }

    public void Refresh() {
        Refresh(200);
    }

    @Test
    public void test() {
        assertEquals("1", "2");
    }

    @Nested
    public class LoginTests {

        @Nested
        public class FirstLoginRequirementTests {

            @BeforeEach
            public void FirstLogin() {
                AuthControllerTests.this.FirstLogin();
            }

            @Test
            public void TestLoginSuccess() {}

            @Test
            public void TestLoginWithWrongToken() {
                ValidateLogin(Map.of("email", admin_email, "password", admin_password),
                        "afdfasfda", 401);
            }

            @Test
            public void TestLoginWithWrongJsonBody() {
                ValidateLogin(Map.of("email", "fdgda", "password", "dagagdg"),
                        accessToken, 401);
            }
        }

        @Test
        public void TestLogoutWithoutBody() {
            ValidateLogin(new HashMap<String, Object>(), null, 400);
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
        public void TestLogout() {
            FirstLogin();
            Logout();
        }

        @Test
        public void TestLogoutWithoutBody() {
            AuthProvider.Logout(mockMvc, backend_url, null, 400);
        }

        @Test
        public void TestLogoutWithWrongToken() {
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
            ValidateRefresh(new HashMap<String, Object>(), 400);
        }

        @Test
        public void TestWithWrongToken() {
            refreshToken = "adsgadgdg";
            Refresh(401);
        }
    }
}
