package education.kub.backend.ce.domain.auth.controller;


import education.kub.backend.ce.app.filter.JwtAuthFilter;
import education.kub.backend.ce.domain.auth.service.AuthService;
import education.kub.backend.ce.infrastructure.properties.executor.ConnectionProperties;
import education.kub.backend.ce.infrastructure.providers.allure.SuiteHierarchy;
import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.components.auth.LoginComponent;
import education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.AuthProvider;
import education.kub.backend.ce.infrastructure.components.user.UserComponent;

import education.kub.backend.ce.infrastructure.providers.mocks.WebMvc.MockMvcProvider;
import education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.LoginProvider;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;

import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.AuthProvider.RefreshTokenToMap;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"education"})
@EnableJpaRepositories(basePackages={"education"})
@TestPropertySource(locations = {"classpath:test.application.properties"})
public class AuthControllerTests {
    @Autowired
    private final ConnectionProperties conn = new ConnectionProperties();

    private final LoginProperties loginData = new LoginProperties();

    private final LoginComponent lComponent = new LoginComponent();
    @Autowired
    private UserComponent uComponent;

    void initMocks() {
        loginData.executor.setConn(conn);
        lComponent.setLoginData(loginData);
        uComponent.setLoginData(loginData);
        uComponent.mockRepos();
        loginData.executor.mvc = MockMvcProvider.createAndSetupMockMvc(
                new JwtAuthFilter(uComponent.jwtTokenProvider, uComponent.tokenStoreService),
                new AuthController(
                        new AuthService(uComponent.userRepo, uComponent.passwordService,
                                uComponent.tokenStoreService, uComponent.jwtTokenProvider)
                )
        );
    }

    @BeforeEach
    public void Setup() {
        SuiteHierarchy.SetAllureTestHierarchy();
        Allure.suite("Auth API Controller Tests");
        initMocks();
    }

    @Step("Login")
    public void ValidateLogin(Map<String, String> request_map, String bearer_token, HttpStatusCode expectedStatusCode) {
        LoginProvider.ValidateLogin(loginData.executor, request_map, bearer_token, expectedStatusCode);
    }

    @Step("Logout")
    public void ValidateLogout(HttpStatusCode expectedStatusCode) {
        AuthProvider.Logout(loginData, expectedStatusCode);
    }

    public void ValidateLogout() {
        ValidateLogout(HttpStatus.NO_CONTENT);
    }

    @Step("Refresh")
    public void ValidateRefresh(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        AuthProvider.Refresh(loginData.executor, request_body, expectedStatusCode,
                "schemas/RefreshResponse.json");;
    }

    public void ValidateRefresh(HttpStatusCode expectedStatusCode) {
        ValidateRefresh(RefreshTokenToMap(loginData.refreshToken), expectedStatusCode);
    }

    public void Refresh() {
        ValidateRefresh(HttpStatus.OK);
    }

    @Nested
    public class LoginTests {

        void SetAllureTestSubSuite() {
            Allure.label("subSuite", "POST /api/v1/auth/login");
        }

        @Test
        @DisplayName("When request is valid, POST /api/v1/auth/login returns 200 and valid response")
        @Description("When request is valid, POST /api/v1/auth/login returns 200 and valid response.")
        public void TestLoginSuccess() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
        }

        @Test
        @DisplayName("When request body with wrong password, POST /api/v1/auth/login returns 401")
        @Description("When request body with wrong password, POST /api/v1/auth/login returns 401.")
        public void TestLoginWithWrongPassword() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(Map.of("email", loginData.email, "password", "dagagdg"),
                    loginData.accessToken, HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request body with wrong email, POST /api/v1/auth/login returns 401")
        @Description("When request body with wrong email, POST /api/v1/auth/login returns 401.")
        public void TestLoginWithWrongEmail() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(Map.of("email", loginData.email, "password", "dagagdg"), loginData.accessToken,
                    HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request body without email, POST /api/v1/auth/login returns 400")
        @Description("When request body without email, POST /api/v1/auth/login returns 400.")
        public void TestLoginWithoutEmail() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(Map.of("password", "dagagdg"), loginData.accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with invalid body, POST /api/v1/auth/login returns 400")
        @Description("When request with invalid body, POST /api/v1/auth/login returns 400.")
        public void TestLoginWithInvalidBody() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(Map.of("afafsfs", loginData.email, "fdfdf", loginData.password),
                    loginData.accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with empty body, POST /api/v1/auth/login returns 400")
        @Description("When request with empty body, POST /api/v1/auth/login returns 400.")
        public void TestLoginWithEmptyBody() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(Map.of(), loginData.accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request without body, POST /api/v1/auth/login returns 400")
        @Description("When request without body, POST /api/v1/auth/login returns 400.")
        public void TestLoginWithoutBody() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(null, loginData.accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with invalid access token header , POST /api/v1/auth/login returns 401")
        @Description("When request with invalid access token header, POST /api/v1/auth/login returns 401.")
        public void TestLoginWithInvalidToken() {
            LoginTests.this.SetAllureTestSubSuite();
            ValidateLogin(Map.of("email", loginData.email, "password", loginData.password),
                    "afdfasfda", HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    public class LogoutTests {

        void SetAllureTestSubSuite() {
            Allure.label("subSuite", "POST /api/v1/auth/logout");
        }

        @Test
        @DisplayName("When request is valid, POST /api/v1/auth/logout returns 204")
        @Description("When request is valid, POST /api/v1/auth/logout returns 204.")
        public void TestLogoutSuccess() {
            LogoutTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogout();
        }

        @Test
        @DisplayName("When request without access token header, POST /api/v1/auth/logout returns 401")
        @Description("When request without access token header, POST /api/v1/auth/logout returns 401.")
        public void TestLogoutWithoutBearerToken() {
            LogoutTests.this.SetAllureTestSubSuite();
            ValidateLogout(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request with invalid access token, POST /api/v1/auth/logout returns 401")
        @Description("When request with invalid access token, POST /api/v1/auth/logout returns 401.")
        public void TestLogoutWithInvalidToken() {
            LogoutTests.this.SetAllureTestSubSuite();
            loginData.accessToken = "adsgadgdg";
            ValidateLogout(HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    public class RefreshTests {

        public void SetAllureTestSubSuite() {
            Allure.label("subSuite", "POST /api/v1/auth/refresh");
        }

        @Test
        @DisplayName("When request is valid, POST /api/v1/auth/refresh returns 200")
        @Description("When request is valid, POST /api/v1/auth/refresh returns 200.")
        public void TestRefreshSuccess() {
            SetAllureTestSubSuite();
            lComponent.FirstLogin();
            Refresh();
        }

        @Test
        @DisplayName("When request without body, POST /api/v1/auth/refresh returns 400")
        @Description("When request without body, POST /api/v1/auth/refresh returns 400.")
        public void TestRefreshWithoutBody() {
            SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateRefresh(null, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with invalid refresh token header, POST /api/v1/auth/refresh returns 401")
        @Description("When request with invalid refresh token header, POST /api/v1/auth/refresh returns 401.")
        public void TestRefreshWithInvalidRefreshToken() {
            RefreshTests.this.SetAllureTestSubSuite();
            loginData.refreshToken = "adsgadgdg";
            ValidateRefresh(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request with empty refresh token header, POST /api/v1/auth/refresh returns 401")
        @Description("When request with empty refresh token header, POST /api/v1/auth/refresh returns 401.")
        public void TestRefreshWithEmptyRefreshToken() {
            RefreshTests.this.SetAllureTestSubSuite();
            loginData.refreshToken = "";
            ValidateRefresh(HttpStatus.UNAUTHORIZED);
        }
    }
}
