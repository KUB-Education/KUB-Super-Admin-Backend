package education.kub.backend.ce.domain.user.controller;

import education.kub.backend.ce.app.filter.JwtAuthFilter;
import education.kub.backend.ce.app.properties.AppAccountRegistrationProperties;
import education.kub.backend.ce.domain.auth.controller.AuthController;
import education.kub.backend.ce.domain.auth.model.LoginRequest;
import education.kub.backend.ce.domain.auth.service.AuthService;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.role.repository.RoleRepository;
import education.kub.backend.ce.domain.user.mapper.UserMapper;
import education.kub.backend.ce.domain.user.model.UserCreateRequest;
import education.kub.backend.ce.domain.user.model.UserPasswordRecoveryRequest;
import education.kub.backend.ce.domain.user.model.UserRegistrationData;
import education.kub.backend.ce.domain.user.model.UserUpdateRequest;
import education.kub.backend.ce.domain.user.service.UserService;
import education.kub.backend.ce.infrastructure.components.auth.LoginComponent;
import education.kub.backend.ce.infrastructure.components.user.UserComponent;
import education.kub.backend.ce.infrastructure.email.service.EmailService;
import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ConnectionProperties;
import education.kub.backend.ce.infrastructure.properties.user.UserProperties;
import education.kub.backend.ce.infrastructure.properties.user.UserRequestUrlParameters;
import education.kub.backend.ce.infrastructure.providers.allure.SuiteHierarchy;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.RoleRepositoryMockProvider;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.UserRepositoryMockProvider;
import education.kub.backend.ce.infrastructure.providers.request_wrappers.user.UserProvider;
import education.kub.backend.ce.infrastructure.token.provider.JwtTokenProvider;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"education"})
@EnableJpaRepositories(basePackages={"education"})
@TestPropertySource(locations = {"classpath:test.application.properties"})
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class UserControllerTests {
    private final LoginProperties loginData = new LoginProperties();
    private final UserProperties user_creator = UserProperties.builder().build();
    private final UserProperties user_to_create = UserProperties.builder().
            id(1L)
            .last_name("New")
            .first_name("created")
            .middle_name("user")
            .email("Test.email@example.com").
            password("new_password").build();
    private final LoginComponent lComponent = new LoginComponent();

    @Autowired
    private ConnectionProperties conn;
    @Autowired
    private UserComponent uComponent;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AppAccountRegistrationProperties appAccountRegistrationProperties;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthController authController;
    @MockitoSpyBean
    private AuthService authService;
    @Autowired
    private UserController userController;
    @MockitoSpyBean
    private UserService userService;
    @MockitoSpyBean
    private JwtAuthFilter jwtAuthFilter;
    @Autowired
    private RoleRepository roleRepository;

    void initMocks() {
        loginData.setUser(user_creator);
        lComponent.executor.setConn(conn);
        lComponent.setLoginData(loginData);
        uComponent.setUserData(loginData.user);
        var user = uComponent.mockRepos();

        lComponent.executor.mvc = mockMvc;

        JwtAuthFilter jwtAuthFilterInjected = new JwtAuthFilter(jwtTokenProvider, uComponent.tokenStoreService);

        AuthService authServiceInjected = new AuthService(uComponent.userRepo, uComponent.passwordService,
                uComponent.tokenStoreService, jwtTokenProvider);

        EmailService emailService = Mockito.mock(EmailService.class);
        Mockito.lenient().when(emailService.sendRegistrationEmail(any(UserRegistrationData.class),any(String.class)))
                .thenReturn(true);

        UserService userServiceInterceptor = new UserService(
                userMapper, uComponent.userRepo, uComponent.roleRepo,emailService,
                uComponent.passwordService, uComponent.tokenStoreService, appAccountRegistrationProperties);

        Mockito.lenient().doAnswer(invocation -> {
            return authServiceInjected.login(invocation.getArgument(0));
        }).when(authService).login(any(LoginRequest.class));

        Mockito.lenient().doAnswer(invocation -> {
            return userServiceInterceptor.createUser(invocation.getArgument(0));
        }).when(userService).createUser(any(UserCreateRequest.class));
        Mockito.lenient().doAnswer(invocation -> {
            return userServiceInterceptor.getUserWithRoles(invocation.getArgument(0));
        }).when(userService).getUserWithRoles(any(long.class));
        Mockito.lenient().doAnswer(invocation -> {
            return userServiceInterceptor.getUsers();
        }).when(userService).getUsers();
        Mockito.lenient().doAnswer(invocation -> {
            return userServiceInterceptor.updateUser(invocation.getArgument(0), invocation.getArgument(1));
        }).when(userService).updateUser(any(long.class), any(UserUpdateRequest.class));
        Mockito.lenient().doAnswer(invocation -> {
            userServiceInterceptor.recoveryUserPassword(invocation.getArgument(0));
            return null;
        }).when(userService).recoveryUserPassword(any(UserPasswordRecoveryRequest.class));
        Mockito.lenient().doAnswer(invocation -> {
            userServiceInterceptor.deleteUser(invocation.getArgument(0));
            return null;
        }).when(userService).deleteUser(any(long.class));
        Mockito.lenient().doAnswer(invocation -> {
            return userServiceInterceptor.sendUserPassword(invocation.getArgument(0));
        }).when(userService).sendUserPassword(any(long.class));
        Mockito.lenient().doAnswer(invocation -> {
            return userServiceInterceptor.addUserRoleById(invocation.getArgument(0), invocation.getArgument(1));
        }).when(userService).addUserRoleById(any(long.class),any(long.class));
        Mockito.lenient().doAnswer(invocation -> {
            return userServiceInterceptor.removeUserRoleById(invocation.getArgument(0), invocation.getArgument(1));
        }).when(userService).removeUserRoleById(any(long.class),any(long.class));

        try {
            Mockito.lenient().doAnswer(invocation -> {
                jwtAuthFilterInjected.doFilter(invocation.getArgument(0),
                        invocation.getArgument(1), invocation.getArgument(2));
                return null;
            }).when(jwtAuthFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
        }
        catch (Exception _) {
        }
    }

    @BeforeEach
    public void Setup() {
        SuiteHierarchy.SetAllureTestHierarchy();
        Allure.suite("User API Controller Tests");
        initMocks();
    }

    @Step("Create user")
    void ValidateCreateUser(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        UserProvider.CreateUser(lComponent.executor, loginData, request_body, expectedStatusCode);
    }

    @Step("Get all users")
    void ValidateGetUsers(HttpStatusCode expectedStatusCode) {
        UserProvider.GetUsers(lComponent.executor, loginData, expectedStatusCode);
    }

    @Step("Get user")
    void ValidateGetUser(UserRequestUrlParameters urlParams, HttpStatusCode expectedStatusCode) {
        UserProvider.GetUser(lComponent.executor, loginData, urlParams, expectedStatusCode);
    }

    @Step("Get user")
    void ValidateGetUser(HttpStatusCode expectedStatusCode) {
        UserProvider.GetUser(lComponent.executor, loginData, loginData.getUser().toUrlProperties(), expectedStatusCode);
    }

    @Step("Update user")
    void ValidateUpdateUser(Map<String, String> request_body, UserRequestUrlParameters urlParams, HttpStatusCode expectedStatusCode) {
        UserProvider.UpdateUser(lComponent.executor, loginData, request_body, urlParams, expectedStatusCode);
    }

    @Step("Update user")
    void ValidateUpdateUser(Map<String, String> request_body, HttpStatusCode expectedStatusCode) {
        UserProvider.UpdateUser(lComponent.executor, loginData, request_body, loginData.getUser().toUrlProperties(), expectedStatusCode);
    }

    @Step("Delete user")
    void ValidateDeleteUser(UserRequestUrlParameters urlParams, HttpStatusCode expectedStatusCode) {
        UserProvider.DeleteUser(lComponent.executor, loginData, urlParams, expectedStatusCode);
    }

    @Step("Delete user")
    void ValidateDeleteUser(HttpStatusCode expectedStatusCode) {
        UserProvider.DeleteUser(lComponent.executor, loginData, loginData.getUser().toUrlProperties(), expectedStatusCode);
    }

    @Step("Resend user password")
    void ValidateResendPassword(UserRequestUrlParameters urlParams, HttpStatusCode expectedStatusCode) {
        UserProvider.ResendPassword(lComponent.executor, loginData, urlParams, expectedStatusCode);
    }

    @Step("Resend user password")
    void ValidateResendPassword(HttpStatusCode expectedStatusCode) {
        UserProvider.ResendPassword(lComponent.executor, loginData, loginData.getUser().toUrlProperties(), expectedStatusCode);
    }

    @Step("Get users roles")
    void ValidateGetUsersRoles(HttpStatusCode expectedStatusCode) {
        UserProvider.GetUsersRoles(lComponent.executor, loginData, expectedStatusCode);
    }

    @Step("Add role")
    void ValidateAddUserRole(UserRequestUrlParameters urlParams, HttpStatusCode expectedStatusCode) {
        UserProvider.AddUserRole(lComponent.executor, loginData, urlParams, expectedStatusCode);
    }

    @Step("Add role")
    void ValidateAddUserRole(HttpStatusCode expectedStatusCode) {
        UserProvider.AddUserRole(lComponent.executor, loginData, loginData.getUser().toUrlProperties(), expectedStatusCode);
    }

    @Step("Remove role")
    void ValidateRemoveUserRole(UserRequestUrlParameters urlParams, HttpStatusCode expectedStatusCode) {
        UserProvider.RemoveUserRole(lComponent.executor, loginData, urlParams, expectedStatusCode);
    }

    @Step("Remove role")
    void ValidateRemoveUserRole(HttpStatusCode expectedStatusCode) {
        UserProvider.RemoveUserRole(lComponent.executor, loginData, loginData.getUser().toUrlProperties(), expectedStatusCode);
    }

    public static Stream<Arguments> PrivilegedRoleTypes() {
        RoleEntity.Type[] privilegedTypes = {RoleEntity.Type.ADMIN, RoleEntity.Type.SYSADMIN, RoleEntity.Type.ORGANIZER};
        return Arrays.stream(privilegedTypes).map(Arguments::of);
    }

    public static Stream<Arguments> NonPrivilegedRoleTypes() {
        RoleEntity.Type[] nonPrivilegedTypes = {RoleEntity.Type.USER, RoleEntity.Type.STUDENT, RoleEntity.Type.LECTURER};
        return Arrays.stream(nonPrivilegedTypes).map(Arguments::of);
    }

    @AfterEach
    void resetMocks() {
        UserRepositoryMockProvider.resetMocks();
        RoleRepositoryMockProvider.resetMocks();
    }

    public static Stream<Arguments> UserRequestFields() {
        String[] field_names = {"first_name", "last_name", "middle_name", "email"};
        return Arrays.stream(field_names).map(Arguments::of);
    }

    @Nested
    public class UsersTests {
        @Nested
        public class CreateUserTests {
            void SetAllureTestSubSuite() {
                Allure.label("subSuite", "POST /api/v1/users");
            }

            @ParameterizedTest
            @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#PrivilegedRoleTypes")
            @DisplayName("When request is valid, POST /api/v1/users returns 200 and valid response")
            @Description("When request is valid, POST /api/v1/users returns 200 and valid response.")
            void CreateUserSuccessWithAllowedRoles(RoleEntity.Type user_creator_role) {
                SetAllureTestSubSuite();
                Allure.parameter("Role of creator", user_creator_role);
                uComponent.addRole(user_creator_role);
                lComponent.FirstLogin();
                ValidateCreateUser(user_to_create.toMap(), HttpStatus.CREATED);
            }

            @Test
            @DisplayName("When valid request body without middle name field, POST /api/v1/users returns 200 and valid response")
            @Description("When valid request body without middle name field, POST /api/v1/users returns 200 and valid response.")
            void CreateUserSuccessOnUserWithoutMiddleName() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                user_to_create.middle_name = null;
                ValidateCreateUser(user_to_create.toMap(), HttpStatus.CREATED);
            }

            @Test
            @DisplayName("When request without body, POST /api/v1/users returns 400")
            @Description("When request without body, POST /api/v1/users returns 400.")
            void CreateUserWithEmptyRequest() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                ValidateCreateUser(Map.of(), HttpStatus.BAD_REQUEST);
            }

            @Test
            @DisplayName("When valid request body with unnecessary field, POST /api/v1/users returns 400")
            @Description("When valid request body with unnecessary field, POST /api/v1/users returns 400.")
            void CreateUserWithAdditionalRequestField() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                var request_map = user_to_create.toMap();
                request_map.put("VgbrsnbrsB", "sfhHFgsDfas");
                ValidateCreateUser(request_map, HttpStatus.BAD_REQUEST);
            }

            @ParameterizedTest
            @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#UserRequestFields")
            @DisplayName("When request body without required field, POST /api/v1/users returns 400")
            @Description("When request body without required field, POST /api/v1/users returns 400.")
            void CreateUserWithoutRequestField(String request_field) {
                SetAllureTestSubSuite();
                Allure.parameter("Request field", request_field);

                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();

                var field_names = List.of("first_name", "last_name", "middle_name", "email");
                var values = List.of("Ada", "Lovelace", "Byron", "ada.lovelace@kub.education");
                Map<String,String> request_body = IntStream.range(0, values.size()).boxed()
                        .collect(Collectors.toMap(field_names::get, values::get));
                request_body.remove(request_field);

                ValidateCreateUser(request_body, HttpStatus.BAD_REQUEST);
            }

            @ParameterizedTest
            @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#UserRequestFields")
            @DisplayName("When request body with empty field, POST /api/v1/users returns 422")
            @Description("When request body with empty field, POST /api/v1/users returns 422.")
            void CreateUserWithEmptyRequestField(String request_field) {
                SetAllureTestSubSuite();
                Allure.parameter("Request field", request_field);

                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();

                var field_names = List.of("first_name", "last_name", "middle_name", "email");
                var values = List.of("Ada", "Lovelace", "Byron", "ada.lovelace@kub.education");
                Map<String,String> request_body = IntStream.range(0, values.size()).boxed()
                        .collect(Collectors.toMap(field_names::get, values::get));
                request_body.put(request_field, "");

                ValidateCreateUser(request_body, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            @ParameterizedTest
            @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#NonPrivilegedRoleTypes")
            @DisplayName("When valid request with missing roles, POST /api/v1/users returns 401")
            @Description("When valid request with missing roles, POST /api/v1/users returns 401.")
            void CreateUserWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                SetAllureTestSubSuite();
                Allure.parameter("Role of creator", user_creator_role);
                uComponent.addRole(user_creator_role);
                lComponent.FirstLogin();
                ValidateCreateUser(user_creator.toMap(), HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When request without access token header, POST /api/v1/users returns 401")
            @Description("When request without access token header, POST /api/v1/users returns 401.")
            void CreateUserWithoutToken() {
                SetAllureTestSubSuite();
                ValidateCreateUser(user_creator.toMap(), HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When request with invalid access token header, POST /api/v1/users returns 401")
            @Description("When request with invalid access token header, POST /api/v1/users returns 401.")
            void CreateUserWithInvalidToken() {
                SetAllureTestSubSuite();
                lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                ValidateCreateUser(user_creator.toMap(), HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When valid request with email field equal to another existing user, POST /api/v1/users returns 409")
            @Description("When valid request with email field equal to another existing user, POST /api/v1/users returns 409.")
            void CreateUserWithSameEmail() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                ValidateCreateUser(user_creator.toMap(), HttpStatus.CONFLICT);
            }
        }

        @Nested
        public class GetUsersTests {
            void SetAllureTestSubSuite() {
                Allure.label("subSuite", "GET /api/v1/users");
            }

            @ParameterizedTest
            @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#PrivilegedRoleTypes")
            @DisplayName("When request is valid, GET /api/v1/users returns 200 and valid response")
            @Description("When request is valid, GET /api/v1/users returns 200 and valid response.")
            void GetUsersSuccessWithAllowedRoles(RoleEntity.Type user_creator_role) {
                SetAllureTestSubSuite();
                Allure.parameter("Role of creator", user_creator_role);
                uComponent.addRole(user_creator_role);
                lComponent.FirstLogin();
                ValidateGetUsers(HttpStatus.OK);
            }

            @ParameterizedTest
            @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#NonPrivilegedRoleTypes")
            @DisplayName("When valid request with missing roles, GET /api/v1/users returns 401")
            @Description("When valid request with missing roles, GET /api/v1/users returns 401.")
            void GetUsersWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                SetAllureTestSubSuite();
                Allure.parameter("Role of creator", user_creator_role);
                uComponent.addRole(user_creator_role);
                lComponent.FirstLogin();
                ValidateGetUsers(HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When request without access token header, GET /api/v1/users returns 401")
            @Description("When request without access token header, GET /api/v1/users returns 401.")
            void GetUsersWithoutToken() {
                SetAllureTestSubSuite();
                ValidateGetUsers(HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When request with invalid access token header, GET /api/v1/users returns 401")
            @Description("When request with invalid access token header, GET /api/v1/users returns 401.")
            void GetUsersWithInvalidToken() {
                SetAllureTestSubSuite();
                lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                ValidateGetUsers(HttpStatus.UNAUTHORIZED);
            }
        }

        @Nested
        public class UserTests {
            @Nested
            public class GetUserTests {
                void SetAllureTestSubSuite() {
                    Allure.label("subSuite", "GET /api/v1/users/{id}");
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#PrivilegedRoleTypes")
                @DisplayName("When request is valid, GET /api/v1/users/{id} returns 200 and valid response")
                @Description("When request is valid, GET /api/v1/users/{id} returns 200 and valid response.")
                void GetUserSuccessWithAllowedRoles(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Role of creator", user_creator_role);
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    ValidateGetUser(HttpStatus.OK);
                }

                @Test
                @DisplayName("When request with invalid id url parameter, GET /api/v1/users/{id} returns 400")
                @Description("When request with invalid id url parameter, GET /api/v1/users/{id} returns 400.")
                void GetUserWithInvalidIdUrlParameter() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateGetUser(UserRequestUrlParameters.builder().user_id("afsfdsfdsfss").build(), HttpStatus.BAD_REQUEST);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#NonPrivilegedRoleTypes")
                @DisplayName("When valid request with missing roles, GET /api/v1/users/{id} returns 401")
                @Description("When valid request with missing roles, GET /api/v1/users/{id} returns 401.")
                void GetUserWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Role of creator", user_creator_role);
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    ValidateGetUser(HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request without access token header, GET /api/v1/users/{id} returns 401")
                @Description("When request without access token header, GET /api/v1/users/{id} returns 401.")
                void GetUserWithoutToken() {
                    SetAllureTestSubSuite();
                    ValidateGetUser(HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with invalid access token header, GET /api/v1/users/{id} returns 401")
                @Description("When request with invalid access token header, GET /api/v1/users/{id} returns 401.")
                void GetUserWithInvalidToken() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                    ValidateGetUser(HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with id url parameter for not existing user, GET /api/v1/users/{id} returns 404")
                @Description("When request with id url parameter for not existing user, GET /api/v1/users/{id} returns 404.")
                void GetUserWithNonExistedUser() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.user.id = 123124314L;
                    ValidateGetUser(HttpStatus.NOT_FOUND);
                }
            }

            @Nested
            public class UpdateUserTests {
                void SetAllureTestSubSuite() {
                    Allure.label("subSuite", "PUT /api/v1/users/{id}");
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#PrivilegedRoleTypes")
                @DisplayName("When request is valid, PUT /api/v1/users/{id} returns 200 and valid response")
                @Description("When request is valid, PUT /api/v1/users/{id} returns 200 and valid response.")
                void UpdateUserSuccessWithAllowedRoles(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    ValidateUpdateUser(user_to_create.toMap(), HttpStatus.OK);
                }

                @Test
                @DisplayName("When request with invalid id url parameter, PUT /api/v1/users/{id} returns 400")
                @Description("When request with invalid id url parameter, PUT /api/v1/users/{id} returns 400.")
                void UpdateUserWithInvalidIdUrlParameter() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateUpdateUser(user_to_create.toMap(), UserRequestUrlParameters.builder().user_id("afsfdsfdsfss").build(),
                            HttpStatus.BAD_REQUEST);
                }

                @Test
                @DisplayName("When request without body, PUT /api/v1/users/{id} returns 400")
                @Description("When request without body, PUT /api/v1/users/{id} returns 400.")
                void UpdateUserWithEmptyRequest() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateUpdateUser(Map.of(), HttpStatus.BAD_REQUEST);
                }

                @Test
                @DisplayName("When valid request body with unnecessary field, PUT /api/v1/users/{id} returns 400")
                @Description("When valid request body with unnecessary field, PUT /api/v1/users/{id} returns 400.")
                void UpdateUserWithAdditionalRequestField() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var user_creator_request_map = user_to_create.toMap();
                    user_creator_request_map.put("VgbrsnbrsB", "sfhHFgsDfas");
                    ValidateUpdateUser(user_creator_request_map, HttpStatus.BAD_REQUEST);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#UserRequestFields")
                @DisplayName("When request body without required field, PUT /api/v1/users/{id} returns 400")
                @Description("When request body without required field, PUT /api/v1/users/{id} returns 400.")
                void UpdateUserWithoutRequestField(String request_field) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Request field", request_field);

                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();

                    var user_creator_request_map = user_to_create.toMap();
                    user_creator_request_map.remove(request_field);

                    ValidateUpdateUser(user_creator_request_map, HttpStatus.BAD_REQUEST);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#UserRequestFields")
                @DisplayName("When request body with empty field, PUT /api/v1/users/{id} returns 422")
                @Description("When request body with empty field, PUT /api/v1/users/{id} returns 422.")
                void UpdateUserWithEmptyRequestField(String request_field) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Request field", request_field);

                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();

                    var user_creator_request_map = user_to_create.toMap();
                    user_creator_request_map.replace(request_field, "");

                    ValidateUpdateUser(user_creator_request_map, HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#NonPrivilegedRoleTypes")
                @DisplayName("When valid request with missing roles, PUT /api/v1/users/{id} returns 401")
                @Description("When valid request with missing roles, PUT /api/v1/users/{id} returns 401.")
                void UpdateUserWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Request field", user_creator_role);
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateUpdateUser(user_creator.toMap(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request without access token header, PUT /api/v1/users/{id} returns 401")
                @Description("When request without access token header, PUT /api/v1/users/{id} returns 401.")
                void UpdateUserWithoutToken() {
                    SetAllureTestSubSuite();
                    ValidateUpdateUser(user_creator.toMap(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with invalid access token header, PUT /api/v1/users/{id} returns returns 401")
                @Description("When request with invalid access token header, PUT /api/v1/users/{id} returns returns 401.")
                void UpdateUserWithInvalidToken() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                    ValidateUpdateUser(user_creator.toMap(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with id url parameter for not existing user, PUT /api/v1/users/{id} returns 404")
                @Description("When request with id url parameter for not existing user, PUT /api/v1/users/{id} returns 404.")
                void UpdateUserWithNonExistedUser() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    user_creator.id = 123124314L;
                    ValidateUpdateUser(user_creator.toMap(), HttpStatus.NOT_FOUND);
                }

                @Test
                @DisplayName("When valid request with email field equal to another existing user, PUT /api/v1/users/{id} returns 409")
                @Description("When valid request with email field equal to another existing user, PUT /api/v1/users/{id} returns 409.")
                void UpdateUserWithSameEmail() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var user_to_create_request_map = user_to_create.toMap();
                    UserProvider.CreateUser(lComponent.executor, loginData, user_to_create_request_map, HttpStatus.CREATED);
                    var user_creator_request_map = user_creator.toMap();
                    user_creator_request_map.replace("email", user_to_create_request_map.get("email"));
                    ValidateUpdateUser(user_creator_request_map, HttpStatus.CONFLICT);
                }
            }

            @Nested
            public class DeleteUserTests{
                void SetAllureTestSubSuite() {
                    Allure.label("subSuite", "DELETE /api/v1/users/{id}");
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#PrivilegedRoleTypes")
                @DisplayName("When request is valid, DELETE /api/v1/users/{id} returns 200")
                @Description("When request is valid, DELETE /api/v1/users/{id} returns 200.")
                void DeleteUserSuccessWithAllowedRole(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    ValidateDeleteUser(user_creator.toUrlProperties(), HttpStatus.NO_CONTENT);
                }

                @Test
                @DisplayName("When request with invalid id url parameter, DELETE /api/v1/users/{id} returns 400")
                @Description("When request with invalid id url parameter, DELETE /api/v1/users/{id} returns 400.")
                void DeleteUserWithInvalidIdUrlParameter() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateDeleteUser(UserRequestUrlParameters.builder().user_id("afsfdsfdsfss").build(), HttpStatus.BAD_REQUEST);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#NonPrivilegedRoleTypes")
                @DisplayName("When valid request with missing roles, DELETE /api/v1/users/{id} returns 401")
                @Description("When valid request with missing roles, DELETE /api/v1/users/{id} returns 401.")
                void DeleteUserWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Role of creator", user_creator_role);
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    ValidateDeleteUser(HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request without access token header, DELETE /api/v1/users/{id} returns 401")
                @Description("When request without access token header, DELETE /api/v1/users/{id} returns 401.")
                void DeleteUserWithoutToken() {
                    SetAllureTestSubSuite();
                    ValidateDeleteUser(HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with invalid access token header, DELETE /api/v1/users/{id} returns 401")
                @Description("When request with invalid access token header, DELETE /api/v1/users/{id} returns 401.")
                void DeleteUserWithInvalidToken() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                    ValidateDeleteUser(HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with id url parameter for not existing user, DELETE /api/v1/users/{id} returns 404")
                @Description("When request with id url parameter for not existing user, DELETE /api/v1/users/{id} returns 404.")
                void DeleteUserWithNonExistedUser() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    user_creator.id = 123124314L;
                    ValidateDeleteUser(HttpStatus.NOT_FOUND);
                }
            }

            @Nested
            public class ResendPasswordTests {
                void SetAllureTestSubSuite() {
                    Allure.label("subSuite", "POST /api/v1/users/{id}/resend");
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#PrivilegedRoleTypes")
                @DisplayName("When request is valid, POST /api/v1/users/{id}/resend returns 200 and valid response")
                @Description("When request is valid, POST /api/v1/users/{id}/resend returns 200 and valid response.")
                void ResendPasswordSuccessWithAllowedRole(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    ValidateResendPassword(HttpStatus.OK);
                }

                @Test
                @DisplayName("When request with invalid id url parameter, POST /api/v1/users/{id}/resend returns 400")
                @Description("When request with invalid id url parameter, POST /api/v1/users/{id}/resend returns 400.")
                void ResendPasswordWithInvalidIdUrlParameter() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateResendPassword(UserRequestUrlParameters.builder().user_id("afsfdsfdsfss").build(), HttpStatus.BAD_REQUEST);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#NonPrivilegedRoleTypes")
                @DisplayName("When valid request with missing roles, POST /api/v1/users/{id}/resend returns 401")
                @Description("When valid request with missing roles, POST /api/v1/users/{id}/resend returns 401.")
                void ResendPasswordWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Role of creator", user_creator_role);
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    ValidateResendPassword(HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request without access token header, POST /api/v1/users/{id}/resend returns 401")
                @Description("When request without access token header, POST /api/v1/users/{id}/resend returns 401.")
                void ResendPasswordWithoutToken() {
                    SetAllureTestSubSuite();
                    ValidateResendPassword(HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with invalid access token header, POST /api/v1/users/{id}/resend returns 401")
                @Description("When request with invalid access token header, POST /api/v1/users/{id}/resend returns 401.")
                void ResendPasswordWithInvalidToken() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                    ValidateResendPassword(HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with id url parameter for not existing user, POST /api/v1/users/{id}/resend returns 404")
                @Description("When request with id url parameter for not existing user, POST /api/v1/users/{id}/resend returns 404.")
                void ResendPasswordWithNonExistedUser() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    loginData.user.id = 123124314L;
                    ValidateResendPassword(HttpStatus.NOT_FOUND);
                }
            }

            @Nested
            public class UserRoleTests {
                @Nested
                public class AddRoleTests {
                    void SetAllureTestSubSuite() {
                        Allure.label("subSuite", "PUT /api/v1/users/{id}/roles/{role-id}");
                    }

                    @ParameterizedTest
                    @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#PrivilegedRoleTypes")
                    @DisplayName("When request is valid, PUT /api/v1/users/{id}/roles/{role-id} returns 200 and valid response")
                    @Description("When request is valid, PUT /api/v1/users/{id}/roles/{role-id} returns 200 and valid response.")
                    void AddRoleSuccessWithAllowedRole(RoleEntity.Type user_creator_role) {
                        SetAllureTestSubSuite();
                        uComponent.addRole(user_creator_role);
                        lComponent.FirstLogin();
                        var urlProps = user_creator.toUrlProperties();
                        var role_id = uComponent.GetFirstNotDublicatedRoleIdString();
                        urlProps.setRole_id(role_id);
                        ValidateAddUserRole(urlProps, HttpStatus.OK);
                    }

                    @ParameterizedTest
                    @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#NonPrivilegedRoleTypes")
                    @DisplayName("When valid request with missing roles, PUT /api/v1/users/{id}/roles/{role-id} returns 401")
                    @Description("When valid request with missing roles, PUT /api/v1/users/{id}/roles/{role-id} returns 401.")
                    void AddRoleWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                        SetAllureTestSubSuite();
                        uComponent.addRole(user_creator_role);
                        lComponent.FirstLogin();
                        var urlProps = user_creator.toUrlProperties();
                        var role_id = uComponent.GetFirstNotDublicatedRoleIdString();
                        urlProps.setRole_id(role_id);
                        ValidateAddUserRole(urlProps, HttpStatus.UNAUTHORIZED);
                    }

                    @Test
                    @DisplayName("When request with invalid user id url parameter, PUT /api/v1/users/{id}/roles/{role-id} returns 400")
                    @Description("When request with invalid user id url parameter, PUT /api/v1/users/{id}/roles/{role-id} returns 400.")
                    void AddRoleWithInvalidUserIdUrlParameter() {
                        SetAllureTestSubSuite();
                        uComponent.addRole(RoleEntity.Type.ADMIN);
                        lComponent.FirstLogin();
                        ValidateAddUserRole(UserRequestUrlParameters.builder().user_id("1d1dcqfce").role_id("0").build(), HttpStatus.BAD_REQUEST);
                    }

                    @Test
                    @DisplayName("When request with invalid role id url parameter, PUT /api/v1/users/{id}/roles/{role-id} returns 400")
                    @Description("When request with invalid role id url parameter, PUT /api/v1/users/{id}/roles/{role-id} returns 400.")
                    void AddRoleWithInvalidRoleIdUrlParameter() {
                        SetAllureTestSubSuite();
                        uComponent.addRole(RoleEntity.Type.ADMIN);
                        lComponent.FirstLogin();
                        ValidateAddUserRole(UserRequestUrlParameters.builder().role_id("1fed1dd3ds").build(), HttpStatus.BAD_REQUEST);
                    }

                    @Test
                    @DisplayName("When request without access token header, PUT /api/v1/users/{id}/roles/{role-id} returns 401")
                    @Description("When request without access token header, PUT /api/v1/users/{id}/roles/{role-id} returns 401.")
                    void AddRoleWithoutToken() {
                        SetAllureTestSubSuite();
                        ValidateAddUserRole(HttpStatus.UNAUTHORIZED);
                    }

                    @Test
                    @DisplayName("When request with invalid access token header, PUT /api/v1/users/{id}/roles/{role-id} returns 401")
                    @Description("When request with invalid access token header, PUT /api/v1/users/{id}/roles/{role-id} returns 401.")
                    void AddRoleWithInvalidToken() {
                        SetAllureTestSubSuite();
                        lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                        ValidateAddUserRole(HttpStatus.UNAUTHORIZED);
                    }

                    @Test
                    @DisplayName("When request with id url parameter for not existing user, PUT /api/v1/users/{id}/roles/{role-id} returns 404")
                    @Description("When request with id url parameter for not existing user, PUT /api/v1/users/{id}/roles/{role-id} returns 404.")
                    void AddRoleWithNonExistedUser() {
                        SetAllureTestSubSuite();
                        uComponent.addRole(RoleEntity.Type.ADMIN);
                        lComponent.FirstLogin();
                        ValidateAddUserRole(UserRequestUrlParameters.builder().user_id("12312443523").role_id("0").build(), HttpStatus.NOT_FOUND);
                    }

                    @Test
                    @DisplayName("When request with role url parameter for not existing user, PUT /api/v1/users/{id}/roles/{role-id} returns 404")
                    @Description("When request with role url parameter for not existing user, PUT /api/v1/users/{id}/roles/{role-id} returns 404.")
                    void AddRoleWithNonExistedRole() {
                        SetAllureTestSubSuite();
                        uComponent.addRole(RoleEntity.Type.ADMIN);
                        lComponent.FirstLogin();
                        var urlProps = user_creator.toUrlProperties();
                        urlProps.setUser_id("12312431241");
                        urlProps.setRole_id(uComponent.GetRoleIdString(RoleEntity.Type.ADMIN));
                        ValidateAddUserRole(urlProps, HttpStatus.NOT_FOUND);
                    }

                    @Test
                    @DisplayName("When valid request with role id url parameter equal to another existing user role, PUT /api/v1/users/{id} returns 409")
                    @Description("When valid request with role id url parameter equal to another existing user role, PUT /api/v1/users/{id} returns 409.")
                    void AddRoleWithRoleDublicate() {
                        SetAllureTestSubSuite();
                        uComponent.addRole(RoleEntity.Type.ADMIN);
                        lComponent.FirstLogin();
                        var urlProps = user_creator.toUrlProperties();
                        urlProps.setRole_id(uComponent.GetRoleIdString(RoleEntity.Type.ADMIN));
                        ValidateAddUserRole(urlProps, HttpStatus.CONFLICT);
                    }
                }

                @Nested
                public class RemoveRoleTests {
                    void SetAllureTestSubSuite() {
                        Allure.label("subSuite", "DELETE /api/v1/users/{id}/roles/{role-id}");
                    }

                    @ParameterizedTest
                    @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#PrivilegedRoleTypes")
                    @DisplayName("When request is valid, DELETE /api/v1/users/{id}/roles/{role-id} returns 200")
                    @Description("When request is valid, DELETE /api/v1/users/{id}/roles/{role-id} returns 200.")
                    void RemoveRoleSuccessWithAllowedRole(RoleEntity.Type user_creator_role) {
                        SetAllureTestSubSuite();
                        uComponent.addRole(user_creator_role);
                        lComponent.FirstLogin();
                        var urlProps = user_creator.toUrlProperties();
                        urlProps.setRole_id(uComponent.GetRoleIdString(user_creator_role));
                        ValidateRemoveUserRole(urlProps, HttpStatus.OK);
                    }

                    @ParameterizedTest
                    @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#NonPrivilegedRoleTypes")
                    @DisplayName("When valid request with missing roles, DELETE /api/v1/users/{id}/roles/{role-id} returns 401")
                    @Description("When valid request with missing roles, DELETE /api/v1/users/{id}/roles/{role-id} returns 401.")
                    void RemoveRoleWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                        SetAllureTestSubSuite();
                        uComponent.addRole(user_creator_role);
                        lComponent.FirstLogin();
                        var urlProps = user_creator.toUrlProperties();
                        urlProps.setRole_id(uComponent.GetRoleIdString(user_creator_role));
                        ValidateRemoveUserRole(urlProps, HttpStatus.UNAUTHORIZED);
                    }

                    @Test
                    @DisplayName("When request with invalid user id url parameter, DELETE /api/v1/users/{id}/roles/{role-id} returns 400")
                    @Description("When request with invalid user id url parameter, DELETE /api/v1/users/{id}/roles/{role-id} returns 400.")
                    void RemoveRoleWithInvalidUserIdUrlParameter() {
                        SetAllureTestSubSuite();
                        uComponent.addRole(RoleEntity.Type.ADMIN);
                        lComponent.FirstLogin();
                        ValidateRemoveUserRole(UserRequestUrlParameters.builder().user_id("1d1dcqfce").role_id("0").build(), HttpStatus.BAD_REQUEST);
                    }

                    @Test
                    @DisplayName("When request with invalid role id url parameter, DELETE /api/v1/users/{id}/roles/{role-id} returns 400")
                    @Description("When request with invalid role id url parameter, DELETE /api/v1/users/{id}/roles/{role-id} returns 400.")
                    void RemoveRoleWithInvalidRoleIdUrlParameter() {
                        SetAllureTestSubSuite();
                        uComponent.addRole(RoleEntity.Type.ADMIN);
                        lComponent.FirstLogin();
                        ValidateRemoveUserRole(UserRequestUrlParameters.builder().role_id("1fed1dd3ds").build(), HttpStatus.BAD_REQUEST);
                    }

                    @Test
                    @DisplayName("When request without access token header, DELETE /api/v1/users/{id}/roles/{role-id} returns 401")
                    @Description("When request without access token header, DELETE /api/v1/users/{id}/roles/{role-id} returns 401.")
                    void RemoveRoleWithoutToken() {
                        SetAllureTestSubSuite();
                        ValidateRemoveUserRole(HttpStatus.UNAUTHORIZED);
                    }

                    @Test
                    @DisplayName("When request with invalid access token header, DELETE /api/v1/users/{id}/roles/{role-id} returns 401")
                    @Description("When request with invalid access token header, DELETE /api/v1/users/{id}/roles/{role-id} returns 401.")
                    void RemoveRoleWithInvalidToken() {
                        SetAllureTestSubSuite();
                        lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                        ValidateRemoveUserRole(HttpStatus.UNAUTHORIZED);
                    }

                    @Test
                    @DisplayName("When request with id url parameter for not existing user, DELETE /api/v1/users/{id}/roles/{role-id} returns 404")
                    @Description("When request with id url parameter for not existing user, DELETE /api/v1/users/{id}/roles/{role-id} returns 404.")
                    void RemoveRoleWithNonExistedUser() {
                        SetAllureTestSubSuite();
                        uComponent.addRole(RoleEntity.Type.ADMIN);
                        lComponent.FirstLogin();
                        var urlProps = user_creator.toUrlProperties();
                        urlProps.setUser_id("12312443523");
                        urlProps.setRole_id(uComponent.GetRoleIdString(RoleEntity.Type.ADMIN));
                        ValidateRemoveUserRole(urlProps, HttpStatus.NOT_FOUND);
                    }

                    @Test
                    @DisplayName("When request with role url parameter for not existing user, DELETE /api/v1/users/{id}/roles/{role-id} returns 404")
                    @Description("When request with role url parameter for not existing user, DELETE /api/v1/users/{id}/roles/{role-id} returns 404.")
                    void RemoveRoleWithNonExistedRole() {
                        SetAllureTestSubSuite();
                        uComponent.addRole(RoleEntity.Type.ADMIN);
                        lComponent.FirstLogin();
                        var urlProps = user_creator.toUrlProperties();
                        urlProps.setRole_id("12312431241");
                        ValidateRemoveUserRole(urlProps, HttpStatus.NOT_FOUND);
                    }
                }
            }
        }
    }

    @Nested
    public class GetRolesTests {
        void SetAllureTestSubSuite() {
            Allure.label("subSuite", "GET /api/v1/roles");
        }

        @ParameterizedTest
        @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#PrivilegedRoleTypes")
        @DisplayName("When request is valid, GET /api/v1/roles returns 200 and valid response")
        @Description("When request is valid, GET /api/v1/roles returns 200 and valid response.")
        void GetRolesSuccessWithAllowedRole(RoleEntity.Type user_creator_role) {
            SetAllureTestSubSuite();
            uComponent.addRole(user_creator_role);
            lComponent.FirstLogin();
            ValidateGetUsersRoles(HttpStatus.OK);
        }

        @ParameterizedTest
        @MethodSource("education.kub.backend.ce.domain.user.controller.UserControllerTests#NonPrivilegedRoleTypes")
        @DisplayName("When valid request with missing roles, GET /api/v1/roles returns 401")
        @Description("When valid request with missing roles, GET /api/v1/roles returns 401.")
        void GetRolesWithNotAllowedRole(RoleEntity.Type user_creator_role) {
            SetAllureTestSubSuite();
            Allure.parameter("Role of creator", user_creator_role);
            uComponent.addRole(user_creator_role);
            lComponent.FirstLogin();
            ValidateGetUsersRoles(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request without access token header, GET /api/v1/roles returns 401")
        @Description("When request without access token header, GET /api/v1/roles returns 401.")
        void GetRolesWithoutToken() {
            SetAllureTestSubSuite();
            ValidateGetUsersRoles(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("When request with invalid access token header, GET /api/v1/roles returns 401")
        @Description("When request with invalid access token header, GET /api/v1/roles returns 401.")
        void GetRolesWithInvalidToken() {
            SetAllureTestSubSuite();
            lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
            ValidateGetUsersRoles(HttpStatus.UNAUTHORIZED);
        }
    }
}
