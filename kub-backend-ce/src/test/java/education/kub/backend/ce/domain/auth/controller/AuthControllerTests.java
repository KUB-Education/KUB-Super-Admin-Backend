package education.kub.backend.ce.domain.auth.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import education.kub.backend.ce.app.exception.handler.GlobalExceptionHandler;
import education.kub.backend.ce.app.filter.JwtAuthFilter;
import education.kub.backend.ce.domain.auth.service.AuthService;
import education.kub.backend.ce.helpers.allure.SuiteHierarchyProvider;
import education.kub.backend.ce.helpers.users.UserProvider;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;

import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"education"})
@EnableJpaRepositories(basePackages={"education"})
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:test.application.properties"})
public class AuthControllerTests extends UserProvider {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    void InitializeMocks() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper( new ObjectMapper().setPropertyNamingStrategy(namingStrategy));
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(
                        new AuthService(userRepo, passwordService,
                                tokenStoreService, jwtTokenProvider)))
                .addFilter(jwtAuthFilter)
                .setControllerAdvice(globalExceptionHandler)
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();
    }

    @BeforeEach
    public void Setup() {
        SuiteHierarchyProvider.SetAllureTestHierarchy();
        Allure.suite("Auth API Controller Tests");
        CreateUser();
        InitializeMocks();
    }

    @Step("Logout")
    public void ValidateLogout(HttpStatusCode expectedStatusCode) {
        AuthProvider.Logout(this, backend_url, accessToken, expectedStatusCode);
    }

    public void Logout() {
        ValidateLogout(HttpStatus.NO_CONTENT);
    }

    @Step("Refresh")
    public void ValidateRefresh(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        AuthProvider.Refresh(this, backend_url, request_body, expectedStatusCode,
                "schemas/RefreshResponse.json");
    }

    public void Refresh(HttpStatusCode expectedStatusCode) {
        ValidateRefresh(Map.of("refresh_token", refreshToken), expectedStatusCode);
    }

    public void Refresh() {
        Refresh(HttpStatus.OK);
    }

    @Nested
    public class LoginTests {

        void SetAllureTestSubSuite() {
            Allure.label("subSuite", "/api/v1/auth/login");
        }

        @Test
        @DisplayName("When request is valid, /api/v1/auth/login returns 200 and valid response")
        @Description("When request is valid, /api/v1/auth/login returns 200 and valid response.")
        public void TestLoginSuccess() {
            LoginTests.this.SetAllureTestSubSuite();
            FirstLogin();
        }

        @Test
        @DisplayName("When request body with wrong password, /api/v1/auth/login returns 401")
        @Description("When request body with wrong password, /api/v1/auth/login returns 401.")
        public void TestLoginWithWrongPassword() {
            LoginTests.this.SetAllureTestSubSuite();
            FirstLogin();
            ValidateLogin(Map.of("email", email, "password", "dagagdg"), accessToken, HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request body with wrong email, /api/v1/auth/login returns 401")
        @Description("When request body with wrong email, /api/v1/auth/login returns 401.")
        public void TestLoginWithWrongEmail() {
            LoginTests.this.SetAllureTestSubSuite();
            FirstLogin();
            ValidateLogin(Map.of("email", email, "password", "dagagdg"), accessToken, HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request body without email, /api/v1/auth/login returns 400")
        @Description("When request body without email, /api/v1/auth/login returns 400.")
        public void TestLoginWithoutEmail() {
            LoginTests.this.SetAllureTestSubSuite();
            FirstLogin();
            ValidateLogin(Map.of("password", "dagagdg"), accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with invalid body, /api/v1/auth/login returns 400")
        @Description("When request with invalid body, /api/v1/auth/login returns 400.")
        public void TestLoginWithInvalidBody() {
            LoginTests.this.SetAllureTestSubSuite();
            FirstLogin();
            ValidateLogin(Map.of("afafsfs", email, "fdfdf", password), accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with empty body, /api/v1/auth/login returns 400")
        @Description("When request with empty body, /api/v1/auth/login returns 400.")
        public void TestLoginWithEmptyBody() {
            LoginTests.this.SetAllureTestSubSuite();
            FirstLogin();
            ValidateLogin(Map.of(), accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request without body, /api/v1/auth/login returns 400")
        @Description("When request without body, /api/v1/auth/login returns 400.")
        public void TestLoginWithoutBody() {
            LoginTests.this.SetAllureTestSubSuite();
            FirstLogin();
            ValidateLogin(null, accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with invalid access token header , /api/v1/auth/login returns 401")
        @Description("When request with invalid access token header, /api/v1/auth/login returns 401.")
        public void TestLoginWithInvalidToken() {
            LoginTests.this.SetAllureTestSubSuite();
            ValidateLogin(Map.of("email", email, "password", password), "afdfasfda", HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    public class LogoutTests {

        void SetAllureTestSubSuite() {
            Allure.label("subSuite", "/api/v1/auth/logout");
        }

        @Test
        @DisplayName("When request is valid, /api/v1/auth/logout returns 204")
        @Description("When request is valid, /api/v1/auth/logout returns 204.")
        public void TestLogoutSuccess() {
            LogoutTests.this.SetAllureTestSubSuite();
            FirstLogin();
            Logout();
        }

        @Test
        @DisplayName("When request without access token header, /api/v1/auth/logout returns 401")
        @Description("When request without access token header, /api/v1/auth/logout returns 401.")
        public void TestLogoutWithoutBearerToken() {
            LogoutTests.this.SetAllureTestSubSuite();
            AuthProvider.Logout(AuthControllerTests.this, backend_url, null, HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request with invalid access token, /api/v1/auth/logout returns 401")
        @Description("When request with invalid access token, /api/v1/auth/logout returns 401.")
        public void TestLogoutWithInvalidToken() {
            LogoutTests.this.SetAllureTestSubSuite();
            accessToken = "adsgadgdg";
            ValidateLogout(HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    public class RefreshTests {

        public void SetAllureTestSubSuite() {
            Allure.label("subSuite", "/api/v1/auth/refresh");
        }

        @Test
        @DisplayName("When request is valid, /api/v1/auth/refresh returns 200")
        @Description("When request is valid, /api/v1/auth/refresh returns 200.")
        public void TestRefreshSuccess() {
            SetAllureTestSubSuite();
            FirstLogin();
            Refresh();
        }

        @Test
        @DisplayName("When request without body, /api/v1/auth/refresh returns 400")
        @Description("When request without body, /api/v1/auth/refresh returns 400.")
        public void TestRefreshWithoutBody() {
            SetAllureTestSubSuite();
            FirstLogin();
            ValidateRefresh(null, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with invalid refresh token header, /api/v1/auth/refresh returns 401")
        @Description("When request with invalid refresh token header, /api/v1/auth/refresh returns 401.")
        public void TestRefreshWithInvalidRefreshToken() {
            RefreshTests.this.SetAllureTestSubSuite();
            refreshToken = "adsgadgdg";
            Refresh(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request with empty refresh token header, /api/v1/auth/refresh returns 401")
        @Description("When request with empty refresh token header, /api/v1/auth/refresh returns 401.")
        public void TestRefreshWithEmptyRefreshToken() {
            RefreshTests.this.SetAllureTestSubSuite();
            refreshToken = "";
            Refresh(HttpStatus.UNAUTHORIZED);
        }
    }
}
