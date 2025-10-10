package education.kub.backend.ce.domain.auth.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import education.kub.backend.ce.app.exception.handler.GlobalExceptionHandler;
import education.kub.backend.ce.app.filter.JwtAuthFilter;
import education.kub.backend.ce.domain.auth.service.AuthService;
import education.kub.backend.ce.helpers.users.UserProvider;

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

    @BeforeEach
    public void initializeMocks() {
        createUser();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper( new ObjectMapper().setPropertyNamingStrategy(namingStrategy));
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(
                new AuthService(userRepo, passwordService,
                        tokenStoreService, jwtTokenProvider)))
                .addFilter(jwtAuthFilter)
                .setControllerAdvice(globalExceptionHandler)
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();
    }

    public void ValidateLogout(HttpStatusCode expectedStatusCode) {
        AuthProvider.Logout(mockMvc, backend_url, accessToken, expectedStatusCode, true);
    }

    public void Logout() {
        ValidateLogout(HttpStatus.NO_CONTENT);
    }

    public void ValidateRefresh(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        AuthProvider.Refresh(mockMvc, backend_url, request_body, expectedStatusCode,
                "schemas/RefreshResponse.json");
    }

    public void Refresh(HttpStatusCode expectedStatusCode) {
        AuthProvider.Refresh(mockMvc, backend_url, Map.of("refresh_token", refreshToken), expectedStatusCode,
                "schemas/RefreshResponse.json");
    }

    public void Refresh() {
        Refresh(HttpStatus.OK);
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
                    "afdfasfda", HttpStatus.UNAUTHORIZED);
        }

        @Test
        public void TestLoginWithWrongPassword() {
            ValidateLogin(Map.of("email", email, "password", "dagagdg"),
                    "afdfasfda", HttpStatus.UNAUTHORIZED);
        }

        @Test
        public void TestLoginWithWrongEmail() {
            ValidateLogin(Map.of("email", email, "password", "dagagdg"),
                    accessToken, HttpStatus.UNAUTHORIZED);
        }

        @Test
        public void TestLoginWithInvalidBody() {
            ValidateLogin(Map.of("afafsfs", email, "fdfdf", password),
                    accessToken, HttpStatus.BAD_REQUEST);
        }

        @Test
        public void TestLogoutWithoutBody() {
            ValidateLogin(null, null, HttpStatus.BAD_REQUEST);
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
            AuthProvider.Logout(mockMvc, backend_url, null, HttpStatus.UNAUTHORIZED, true);
        }

        @Test
        public void TestLogoutWithoutAuthorizationHeader() {
            AuthProvider.Logout(mockMvc, backend_url, null, HttpStatus.UNAUTHORIZED, false);
        }

        @Test
        public void TestLogoutWithInvalidToken() {
            accessToken = "adsgadgdg";
            ValidateLogout(HttpStatus.UNAUTHORIZED);
        }

        @Test
        public void TestLogoutWithoutLogin() {
            ValidateLogout(HttpStatus.UNAUTHORIZED);
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
            ValidateRefresh(null, HttpStatus.BAD_REQUEST);
        }

        @Test
        public void TestWithInvalidRefreshToken() {
            FirstLogin();
            refreshToken = "adsgadgdg";
            Refresh(HttpStatus.UNAUTHORIZED);
        }
    }
}
