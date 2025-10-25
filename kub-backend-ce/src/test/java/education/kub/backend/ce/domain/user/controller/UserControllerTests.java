package education.kub.backend.ce.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import education.kub.backend.ce.app.exception.handler.GlobalExceptionHandler;
import education.kub.backend.ce.app.filter.JwtAuthFilter;
import education.kub.backend.ce.app.properties.AppAccountRegistrationProperties;
import education.kub.backend.ce.domain.auth.controller.AuthController;
import education.kub.backend.ce.domain.auth.service.AuthService;
import education.kub.backend.ce.domain.role.repository.RoleRepository;
import education.kub.backend.ce.domain.user.mapper.UserMapper;
import education.kub.backend.ce.domain.user.service.UserService;
import education.kub.backend.ce.helpers.allure.SuiteHierarchyProvider;
import education.kub.backend.ce.helpers.users.UserProvider;
import education.kub.backend.ce.infrastructure.email.service.EmailService;
import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.infrastructure.token.provider.JwtTokenProvider;
import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
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
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"education"})
@EnableJpaRepositories(basePackages={"education"})
@TestPropertySource(locations = {"classpath:test.application.properties"})
public class UserControllerTests extends UserProvider {

    private final String new_password = "password";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private TokenStoreService tokenStoreService;
    @Autowired
    private AppAccountRegistrationProperties appAccountRegistrationProperties;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    void InitializeMocks() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper( new ObjectMapper().setPropertyNamingStrategy(namingStrategy));
        UserService mockUserService = Mockito.spy(new UserService(userMapper,userRepo,roleRepository,emailService,passwordService,
                tokenStoreService,appAccountRegistrationProperties));
        mvc = MockMvcBuilders.standaloneSetup(new UserAccountController(mockUserService),
                        new AuthController(new AuthService(userRepo, passwordService, tokenStoreService, jwtTokenProvider)))
                .addFilter(jwtAuthFilter)
                .setControllerAdvice(globalExceptionHandler)
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();
    }

    @BeforeEach
    public void Setup() {
        SuiteHierarchyProvider.SetAllureTestHierarchy();
        Allure.suite("Account API Controller Tests");
        CreateUser();
        InitializeMocks();
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Step("Get account info")
    void ValidateUserAccountInfoResponse(HttpStatusCode expectedStatusCode) {
        AccountProvider.GetUserAccountInfo(this, backend_url, accessToken, expectedStatusCode);
    }

    @Step("Change password")
    void ValidateChangePasswordResponse(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        AccountProvider.ChangePassword(this, backend_url, request_body, accessToken, expectedStatusCode);
    }

    @Step("Recover password")
    void ValidateRecoverPasswordResponse(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        AccountProvider.RecoverPassword(this, backend_url, request_body, accessToken, expectedStatusCode);
    }

    @Nested
    public class AccountMeTests {

        void SetAllureTestSubSuite() {
            Allure.label("subSuite", "/api/v1/account/me");
        }

        @Test
        @DisplayName("When request is valid, /api/v1/account/me returns 200 and valid response")
        @Description("When request is valid, /api/v1/account/me returns 200 and valid response.")
        public void GetUserAccountInfoSuccess() {
            SetAllureTestSubSuite();
            FirstLogin();
            ValidateUserAccountInfoResponse(HttpStatus.OK);
        }

        @Test
        @DisplayName("When request without access token header, /api/v1/account/me returns 401")
        @Description("When request without access token header, /api/v1/account/me returns 401.")
        public void GetUserAccountInfoWithoutToken() {
            SetAllureTestSubSuite();
            ValidateUserAccountInfoResponse(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request without invalid access token header, /api/v1/account/me returns 401")
        @Description("When request without invalid access token header, /api/v1/account/me returns 401.")
        public void GetUserAccountInfoWithInvalidToken() {
            SetAllureTestSubSuite();
            accessToken = "gdagddfgadg";
            ValidateUserAccountInfoResponse(HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    public class ChangePasswordTests {

        void SetAllureTestSubSuite() {
            Allure.label("subSuite", "/api/v1/account/change-password");
        }

        @Test
        @DisplayName("When request is valid, /api/v1/account/change-password returns 204 and valid response")
        @Description("When request is valid, /api/v1/account/change-password returns 204 and valid response.")
        public void ChangePasswordSuccess() {
            SetAllureTestSubSuite();
            FirstLogin();
            ValidateChangePasswordResponse(Map.of("old_password", password, "new_password", new_password), HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("When request without body, /api/v1/account/change-password returns 400")
        @Description("When request without body, /api/v1/account/change-password returns 400.")
        public void ChangePasswordWithoutBody() {
            SetAllureTestSubSuite();
            FirstLogin();
            ValidateChangePasswordResponse(null, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with empty body, /api/v1/account/change-password returns 400")
        @Description("When request with empty body, /api/v1/account/change-password returns 400.")
        public void ChangePasswordWithEmptyBody() {
            SetAllureTestSubSuite();
            FirstLogin();
            ValidateChangePasswordResponse(Map.of(), HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request body without old password, /api/v1/account/change-password returns 401")
        @Description("When request body without old password, /api/v1/account/change-password returns 401.")
        public void ChangePasswordWithoutOldPassword() {
            SetAllureTestSubSuite();
            FirstLogin();
            ValidateChangePasswordResponse(Map.of("new_password", new_password), HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request body without new password, /api/v1/account/change-password returns 400")
        @Description("When request body without new password, /api/v1/account/change-password returns 400.")
        public void ChangePasswordWithoutNewPassword() {
            SetAllureTestSubSuite();
            FirstLogin();
            ValidateChangePasswordResponse(Map.of("old_password", password), HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request body with empty new password, /api/v1/account/change-password returns 422")
        @Description("When request body with empty new password, /api/v1/account/change-password returns 422.")
        public void ChangePasswordWithEmptyNewPassword() {
            SetAllureTestSubSuite();
            FirstLogin();
            ValidateChangePasswordResponse(Map.of("old_password", password, "new_password", ""), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        @Test
        @DisplayName("When request body with empty old password, /api/v1/account/change-password returns 401")
        @Description("When request body with empty old password, /api/v1/account/change-password returns 401.")
        public void ChangePasswordWithEmptyOldPassword() {
            SetAllureTestSubSuite();
            FirstLogin();
            ValidateChangePasswordResponse(Map.of("old_password", "", "new_password", new_password), HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request without access token header, /api/v1/account/change-password returns 401")
        @Description("When request without access token header, /api/v1/account/change-password returns 401.")
        public void ChangePasswordWithoutToken() {
            SetAllureTestSubSuite();
            ValidateChangePasswordResponse(Map.of("old_password", password, "new_password", new_password), HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request with invalid access token header, /api/v1/account/change-password returns 401")
        @Description("When request with invalid access token header, /api/v1/account/change-password returns 401.")
        public void ChangePasswordWithInvalidToken() {
            SetAllureTestSubSuite();
            accessToken = "gdagddfgadg";
            ValidateChangePasswordResponse(Map.of("old_password", password, "new_password", new_password), HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    class PasswordRecoveryTests {

        void SetAllureTestSubSuite() {
            Allure.label("subSuite", "/api/v1/account/recovery-password");
        }

        @Test
        @DisplayName("When request is valid, /api/v1/account/recovery-password returns 204 and valid response")
        @Description("When request is valid, /api/v1/account/recovery-password returns 204 and valid response.")
        public void RecoverPasswordSuccess() {
            SetAllureTestSubSuite();
            ValidateRecoverPasswordResponse(Map.of("email", email), HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("When request without body, /api/v1/account/recovery-password returns 400")
        @Description("When request without body, /api/v1/account/recovery-password returns 400.")
        public void RecoverPasswordWithoutBody() {
            SetAllureTestSubSuite();
            ValidateRecoverPasswordResponse(null, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with empty body, /api/v1/account/recovery-password returns 400")
        @Description("When request with empty body, /api/v1/account/recovery-password returns 400.")
        public void RecoverPasswordWithEmptyBody() {
            SetAllureTestSubSuite();
            ValidateRecoverPasswordResponse(Map.of(), HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request body with empty email, /api/v1/account/recovery-password returns 422")
        @Description("When request body with empty email, /api/v1/account/recovery-password returns 422.")
        public void RecoverPasswordWithEmptyEmail() {
            SetAllureTestSubSuite();
            ValidateRecoverPasswordResponse(Map.of("email", ""), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        @Test
        @DisplayName("When request body with invalid email, /api/v1/account/recovery-password returns 422")
        @Description("When request body with invalid email, /api/v1/account/recovery-password returns 422.")
        public void RecoverPasswordWithInvalidEmail() {
            SetAllureTestSubSuite();
            ValidateRecoverPasswordResponse(Map.of("email", "affafdad"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
