package education.kub.backend.ce.domain.auth.controller;


import education.kub.backend.ce.app.filter.JwtAuthFilter;
import education.kub.backend.ce.domain.auth.service.AuthService;
import education.kub.backend.ce.infrastructure.components.auth.LoginComponent;
import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ConnectionProperties;
import education.kub.backend.ce.infrastructure.providers.allure.SuiteHierarchy;
import education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.AuthProvider;
import education.kub.backend.ce.infrastructure.components.user.UserComponent;

import education.kub.backend.ce.infrastructure.providers.mocks.WebMvc.MockMvcProvider;
import education.kub.backend.ce.infrastructure.providers.request_wrappers.auth.LoginProvider;
import education.kub.backend.ce.infrastructure.token.provider.JwtTokenProvider;
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
    private final LoginProperties loginData = new LoginProperties();
    private final LoginComponent lComponent = new LoginComponent();
    @Autowired
    private ConnectionProperties conn;
    @Autowired
    private UserComponent uComponent;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    void initMocks() {
        lComponent.executor.setConn(conn);
        lComponent.setLoginData(loginData);
        uComponent.setUserData(loginData.user);
        uComponent.mockRepos();
        lComponent.executor.mvc = MockMvcProvider.createAndSetupMockMvc(
                new JwtAuthFilter(jwtTokenProvider, uComponent.tokenStoreService),
                new AuthController(
                        new AuthService(uComponent.userRepo, uComponent.passwordService,
                                uComponent.tokenStoreService, jwtTokenProvider)
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
    public void ValidateLogin(Map<String, Object> request_map, String bearer_token, HttpStatusCode expectedStatusCode) {
        LoginProvider.ValidateLogin(lComponent.executor, request_map, bearer_token, expectedStatusCode);
    }

    @Step("Logout")
    public void ValidateLogout(HttpStatusCode expectedStatusCode) {
        AuthProvider.Logout(lComponent.executor, loginData, expectedStatusCode);
    }

    public void ValidateLogout() {
        ValidateLogout(HttpStatus.NO_CONTENT);
    }

    @Step("Refresh")
    public void ValidateRefresh(Map<String, Object> request_body, HttpStatusCode expectedStatusCode) {
        AuthProvider.Refresh(lComponent.executor, request_body, expectedStatusCode,
                "schemas/auth/RefreshResponse.json");
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
        public void LoginSuccess() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
        }

        @Test
        @DisplayName("When request body with wrong password, POST /api/v1/auth/login returns 401")
        @Description("When request body with wrong password, POST /api/v1/auth/login returns 401.")
        public void LoginWithWrongPassword() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(Map.of("email", loginData.user.email, "password", "dagagdg"),
                    loginData.accessToken, HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request body with wrong email, POST /api/v1/auth/login returns 401")
        @Description("When request body with wrong email, POST /api/v1/auth/login returns 401.")
        public void LoginWithWrongEmail() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(Map.of("email", loginData.user.email, "password", "dagagdg"), loginData.accessToken,
                    HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request body without email, POST /api/v1/auth/login returns 400")
        @Description("When request body without email, POST /api/v1/auth/login returns 400.")
        public void LoginWithoutEmail() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(Map.of("password", "dagagdg"), loginData.accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with invalid body, POST /api/v1/auth/login returns 400")
        @Description("When request with invalid body, POST /api/v1/auth/login returns 400.")
        public void LoginWithInvalidBody() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(Map.of("afafsfs", loginData.user.email, "fdfdf", loginData.user.password),
                    loginData.accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with empty body, POST /api/v1/auth/login returns 400")
        @Description("When request with empty body, POST /api/v1/auth/login returns 400.")
        public void LoginWithEmptyBody() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(Map.of(), loginData.accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request without body, POST /api/v1/auth/login returns 400")
        @Description("When request without body, POST /api/v1/auth/login returns 400.")
        public void LoginWithoutBody() {
            LoginTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogin(null, loginData.accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with invalid access token header , POST /api/v1/auth/login returns 401")
        @Description("When request with invalid access token header, POST /api/v1/auth/login returns 401.")
        public void LoginWithInvalidToken() {
            LoginTests.this.SetAllureTestSubSuite();
            ValidateLogin(Map.of("email", loginData.user.email, "password", loginData.user.password),
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
        public void LogoutSuccess() {
            LogoutTests.this.SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateLogout();
        }

        @Test
        @DisplayName("When request without access token header, POST /api/v1/auth/logout returns 401")
        @Description("When request without access token header, POST /api/v1/auth/logout returns 401.")
        public void LogoutWithoutBearerToken() {
            LogoutTests.this.SetAllureTestSubSuite();
            ValidateLogout(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request with invalid access token, POST /api/v1/auth/logout returns 401")
        @Description("When request with invalid access token, POST /api/v1/auth/logout returns 401.")
        public void LogoutWithInvalidToken() {
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
        public void RefreshSuccess() {
            SetAllureTestSubSuite();
            lComponent.FirstLogin();
            Refresh();
        }

        @Test
        @DisplayName("When request without body, POST /api/v1/auth/refresh returns 400")
        @Description("When request without body, POST /api/v1/auth/refresh returns 400.")
        public void RefreshWithoutBody() {
            SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateRefresh(null, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with invalid refresh token header, POST /api/v1/auth/refresh returns 401")
        @Description("When request with invalid refresh token header, POST /api/v1/auth/refresh returns 401.")
        public void RefreshWithInvalidRefreshToken() {
            RefreshTests.this.SetAllureTestSubSuite();
            loginData.refreshToken = "adsgadgdg";
            ValidateRefresh(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request with empty refresh token header, POST /api/v1/auth/refresh returns 401")
        @Description("When request with empty refresh token header, POST /api/v1/auth/refresh returns 401.")
        public void RefreshWithEmptyRefreshToken() {
            RefreshTests.this.SetAllureTestSubSuite();
            loginData.refreshToken = "";
            ValidateRefresh(HttpStatus.UNAUTHORIZED);
        }
    }
}
