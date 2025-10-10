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
import education.kub.backend.ce.helpers.users.UserProvider;
import education.kub.backend.ce.infrastructure.email.service.EmailService;
import education.kub.backend.ce.infrastructure.password.service.PasswordService;
import education.kub.backend.ce.infrastructure.token.provider.JwtTokenProvider;
import education.kub.backend.ce.infrastructure.token.store.service.TokenStoreService;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"education"})
@EnableJpaRepositories(basePackages={"education"})
@AutoConfigureMockMvc
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

    @BeforeEach
    public void initializeMocks() {
        createUser();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper( new ObjectMapper().setPropertyNamingStrategy(namingStrategy));
        UserService mockUserService = Mockito.spy(new UserService(userMapper,userRepo,roleRepository,emailService,passwordService,
                                        tokenStoreService,appAccountRegistrationProperties));
        mockMvc = MockMvcBuilders.standaloneSetup(new UserAccountController(mockUserService),
                new AuthController(new AuthService(userRepo, passwordService, tokenStoreService, jwtTokenProvider)))
                .addFilter(jwtAuthFilter)
                .setControllerAdvice(globalExceptionHandler)
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    void ValidateUserAccountInfoResponse(HttpStatusCode expectedStatusCode) {
        AccountProvider.GetUserAccountInfo(mockMvc, backend_url, accessToken, expectedStatusCode);
    }

    void ValidateChangePasswordResponse(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        AccountProvider.ChangePassword(mockMvc, backend_url, request_body, accessToken, expectedStatusCode);
    }

    void ValidateRecoverPasswordResponse(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        AccountProvider.RecoverPassword(mockMvc, backend_url, request_body, accessToken, expectedStatusCode);
    }


    @Nested
    public class AccountMeTests {

        @Test
        @WithMockUser
        public void GetUserAccountInfoSuccess() {
            FirstLogin();
            ValidateUserAccountInfoResponse(HttpStatus.OK);
        }

        @Test
        public void GetUserAccountInfoWithoutToken() {
            ValidateUserAccountInfoResponse(HttpStatus.UNAUTHORIZED);
        }

        @Test
        public void GetUserAccountInfoWithInvalidToken() {
            accessToken = "gdagddfgadg";
            ValidateUserAccountInfoResponse(HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    public class ChangePasswordTests {

        @Nested
        public class FirstLoginRequirementTests {
            @BeforeEach
            public void FirstLogin() {
                UserControllerTests.this.FirstLogin();
            }

            @Test
            public void ChangePasswordSuccess() {
                ValidateChangePasswordResponse(Map.of("old_password", password, "new_password", new_password), HttpStatus.NO_CONTENT);
            }

            @Test
            public void ChangePasswordWithoutBody() {
                ValidateChangePasswordResponse(null, HttpStatus.BAD_REQUEST);
            }

            @Test
            public void ChangePasswordWithEmptyBody() {
                ValidateChangePasswordResponse(Map.of(), HttpStatus.BAD_REQUEST);
            }

            @Test
            public void ChangePasswordWithoutOldPassword() {
                ValidateChangePasswordResponse(Map.of("new_password", new_password), HttpStatus.UNAUTHORIZED);
            }

            @Test
            public void ChangePasswordWithoutNewPassword() {
                ValidateChangePasswordResponse(Map.of("password", password), HttpStatus.BAD_REQUEST);
            }

            @Test
            public void ChangePasswordWithEmptyNewPassword() {
                ValidateChangePasswordResponse(Map.of("old_password", password, "new_password", ""), HttpStatus.UNPROCESSABLE_ENTITY);
            }

            @Test
            public void ChangePasswordWithEmptyOldPassword() {
                ValidateChangePasswordResponse(Map.of("old_password", "", "new_password", new_password), HttpStatus.UNAUTHORIZED);
            }
        }

        @Test
        public void ChangePasswordWithoutToken() {
            ValidateChangePasswordResponse(Map.of("old_password", password, "new_password", new_password), HttpStatus.UNAUTHORIZED);
        }

        @Test
        public void ChangePasswordWithInvalidToken() {
            accessToken = "gdagddfgadg";
            ValidateChangePasswordResponse(Map.of("old_password", password, "new_password", new_password), HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    class PasswordRecoveryTests {

        @Test
        public void RecoverPasswordSuccess() {
            ValidateRecoverPasswordResponse(Map.of("email", email), HttpStatus.NO_CONTENT);
        }

        @Test
        public void RecoverPasswordWithoutBody() {
            ValidateRecoverPasswordResponse(null, HttpStatus.BAD_REQUEST);
        }

        @Test
        public void RecoverPasswordWithEmptyBody() {
            ValidateRecoverPasswordResponse(Map.of(), HttpStatus.BAD_REQUEST);
        }

        @Test
        public void RecoverPasswordWithEmptyEmail() {
            ValidateRecoverPasswordResponse(Map.of("email", ""), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        @Test
        public void RecoverPasswordWithInvalidEmail() {
            ValidateRecoverPasswordResponse(Map.of("email", "affafdad"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
