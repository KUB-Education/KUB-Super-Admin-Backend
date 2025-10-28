package education.kub.backend.ce.domain.user.controller;

import education.kub.backend.ce.app.filter.JwtAuthFilter;
import education.kub.backend.ce.app.properties.AppAccountRegistrationProperties;
import education.kub.backend.ce.domain.auth.controller.AuthController;
import education.kub.backend.ce.domain.auth.service.AuthService;
import education.kub.backend.ce.domain.user.mapper.UserMapper;
import education.kub.backend.ce.domain.user.service.UserService;
import education.kub.backend.ce.infrastructure.properties.executor.ConnectionProperties;
import education.kub.backend.ce.infrastructure.providers.allure.SuiteHierarchy;
import education.kub.backend.ce.infrastructure.properties.user.AccountRequestProperties;
import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.components.auth.LoginComponent;
import education.kub.backend.ce.infrastructure.providers.request_wrappers.user.AccountProvider;
import education.kub.backend.ce.infrastructure.components.user.UserComponent;
import education.kub.backend.ce.infrastructure.providers.mocks.WebMvc.MockMvcProvider;
import education.kub.backend.ce.infrastructure.email.service.EmailService;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"education"})
@EnableJpaRepositories(basePackages={"education"})
@TestPropertySource(locations = {"classpath:test.application.properties"})
public class UserControllerTests {
    @Autowired
    private ConnectionProperties conn;

    private final LoginProperties loginData = new LoginProperties();
    @Autowired
    private UserComponent uComponent;

    private final AccountRequestProperties params = new AccountRequestProperties();

    private final LoginComponent lComponent = new LoginComponent();
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AppAccountRegistrationProperties appAccountRegistrationProperties;

    void initMocks() {
        loginData.executor.setConn(conn);
        lComponent.setLoginData(loginData);
        uComponent.setLoginData(loginData);
        uComponent.mockRepos();
        loginData.executor.mvc = MockMvcProvider.createAndSetupMockMvc(
                new JwtAuthFilter(uComponent.jwtTokenProvider, uComponent.tokenStoreService),
                new UserAccountController(
                        new UserService(userMapper, uComponent.userRepo, uComponent.roleRepo,emailService,
                                uComponent.passwordService, uComponent.tokenStoreService, appAccountRegistrationProperties)
                ),
                new AuthController(
                        new AuthService(uComponent.userRepo, uComponent.passwordService,
                                uComponent.tokenStoreService, uComponent.jwtTokenProvider
                        )
                )
        );
    }

    @BeforeEach
    public void Setup() {
        SuiteHierarchy.SetAllureTestHierarchy();
        Allure.suite("Account API Controller Tests");
        initMocks();
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Step("Get account info")
    void ValidateUserAccountInfoResponse(HttpStatusCode expectedStatusCode) {
        AccountProvider.GetUserAccountInfo(loginData, expectedStatusCode);
    }

    @Step("Change password")
    void ValidateChangePasswordResponse(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        AccountProvider.ChangePassword(loginData, request_body, expectedStatusCode);
    }

    @Step("Recover password")
    void ValidateRecoverPasswordResponse(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        AccountProvider.RecoverPassword(loginData, request_body, expectedStatusCode);
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
            lComponent.FirstLogin();
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
            loginData.accessToken = "gdagddfgadg";
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
            lComponent.FirstLogin();
            ValidateChangePasswordResponse(
                    Map.of("old_password", loginData.password, "new_password", params.new_password),
                    HttpStatus.NO_CONTENT
            );
        }

        @Test
        @DisplayName("When request without body, /api/v1/account/change-password returns 400")
        @Description("When request without body, /api/v1/account/change-password returns 400.")
        public void ChangePasswordWithoutBody() {
            SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateChangePasswordResponse(null, HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request with empty body, /api/v1/account/change-password returns 400")
        @Description("When request with empty body, /api/v1/account/change-password returns 400.")
        public void ChangePasswordWithEmptyBody() {
            SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateChangePasswordResponse(Map.of(), HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request body without old password, /api/v1/account/change-password returns 401")
        @Description("When request body without old password, /api/v1/account/change-password returns 401.")
        public void ChangePasswordWithoutOldPassword() {
            SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateChangePasswordResponse(Map.of("new_password", params.new_password), HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request body without new password, /api/v1/account/change-password returns 400")
        @Description("When request body without new password, /api/v1/account/change-password returns 400.")
        public void ChangePasswordWithoutNewPassword() {
            SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateChangePasswordResponse(Map.of("old_password", loginData.password), HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("When request body with empty new password, /api/v1/account/change-password returns 422")
        @Description("When request body with empty new password, /api/v1/account/change-password returns 422.")
        public void ChangePasswordWithEmptyNewPassword() {
            SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateChangePasswordResponse(Map.of("old_password", loginData.password, "new_password", ""),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        @Test
        @DisplayName("When request body with empty old password, /api/v1/account/change-password returns 401")
        @Description("When request body with empty old password, /api/v1/account/change-password returns 401.")
        public void ChangePasswordWithEmptyOldPassword() {
            SetAllureTestSubSuite();
            lComponent.FirstLogin();
            ValidateChangePasswordResponse(Map.of("old_password", "", "new_password", params.new_password),
                    HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request without access token header, /api/v1/account/change-password returns 401")
        @Description("When request without access token header, /api/v1/account/change-password returns 401.")
        public void ChangePasswordWithoutToken() {
                SetAllureTestSubSuite();
            ValidateChangePasswordResponse(
                    Map.of("old_password", loginData.password, "new_password", params.new_password),
                    HttpStatus.UNAUTHORIZED
            );
        }

        @Test
        @DisplayName("When request with invalid access token header, /api/v1/account/change-password returns 401")
        @Description("When request with invalid access token header, /api/v1/account/change-password returns 401.")
        public void ChangePasswordWithInvalidToken() {
            SetAllureTestSubSuite();
            loginData.accessToken = "gdagddfgadg";
            ValidateChangePasswordResponse(
                    Map.of("old_password", loginData.password, "new_password", params.new_password),
                    HttpStatus.UNAUTHORIZED
            );
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
            ValidateRecoverPasswordResponse(Map.of("email", loginData.email), HttpStatus.NO_CONTENT);
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
