package education.kub.backend.ce.domain.room.controller;

import education.kub.backend.ce.app.filter.JwtAuthFilter;
import education.kub.backend.ce.app.properties.AppAccountRegistrationProperties;
import education.kub.backend.ce.domain.auth.controller.AuthController;
import education.kub.backend.ce.domain.auth.model.LoginRequest;
import education.kub.backend.ce.domain.auth.service.AuthService;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.room.model.RoomCreateRequest;
import education.kub.backend.ce.domain.room.model.RoomRequestFilter;
import education.kub.backend.ce.domain.room.model.RoomUpdateRequest;
import education.kub.backend.ce.domain.room.service.RoomService;
import education.kub.backend.ce.domain.user.mapper.UserMapper;
import education.kub.backend.ce.infrastructure.components.auth.LoginComponent;
import education.kub.backend.ce.infrastructure.components.room.RoomComponent;
import education.kub.backend.ce.infrastructure.components.user.UserComponent;
import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.room.RoomProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ConnectionProperties;
import education.kub.backend.ce.infrastructure.properties.room.RoomRequestUrlParameters;
import education.kub.backend.ce.infrastructure.properties.user.UserProperties;
import education.kub.backend.ce.infrastructure.providers.allure.SuiteHierarchy;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.*;
import education.kub.backend.ce.infrastructure.providers.request_wrappers.domain.RoomProvider;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"education"})
@EnableJpaRepositories(basePackages={"education"})
@TestPropertySource(locations = {"classpath:test.application.properties"})
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class RoomControllerTests {
    private final LoginProperties loginData = new LoginProperties();
    private final LoginComponent lComponent = new LoginComponent();

    private final UserProperties user_creator = UserProperties.builder().build();

    private final RoomProperties room = RoomProperties.builder().build();

    @Autowired
    private ConnectionProperties conn;
    @Autowired
    private UserComponent uComponent;
    @Autowired
    private RoomComponent roomComponent;
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
    @MockitoBean
    private AuthService authService;
    @Autowired
    private RoomController roomController;
    @MockitoBean
    private RoomService roomService;
    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    public void initMocks() {
        loginData.setUser(user_creator);
        lComponent.executor.setConn(conn);
        lComponent.setLoginData(loginData);
        uComponent.setUserData(loginData.user);
        uComponent.mockRepos();
        roomComponent.mockRepo();

        lComponent.executor.mvc = mockMvc;

        JwtAuthFilter jwtAuthFilterInjected = new JwtAuthFilter(jwtTokenProvider, uComponent.tokenStoreService);

        AuthService authServiceInjected = new AuthService(uComponent.userRepo, uComponent.passwordService,
                uComponent.tokenStoreService, jwtTokenProvider);

        RoomService roomServiceInjected = roomComponent.BuildRoomService();

        Mockito.lenient().doAnswer(invocation -> {
            return authServiceInjected.login(invocation.getArgument(0));
        }).when(authService).login(any(LoginRequest.class));

        Mockito.lenient().doAnswer(invocation -> {
            return roomServiceInjected.createRoom(invocation.getArgument(0));
        }).when(roomService).createRoom(any(RoomCreateRequest.class));
        Mockito.lenient().doAnswer(invocation -> {
            return roomServiceInjected.getRoomById(invocation.getArgument(0));
        }).when(roomService).getRoomById(any(Long.class));
        Mockito.lenient().doAnswer(invocation -> {
            return roomServiceInjected.getRoomsByFilter(invocation.getArgument(0));
        }).when(roomService).getRoomsByFilter(any(RoomRequestFilter.class));
        Mockito.lenient().doAnswer(invocation -> {
            return roomServiceInjected.updateRoom(invocation.getArgument(0), invocation.getArgument(1));
        }).when(roomService).updateRoom(any(Long.class), any(RoomUpdateRequest.class));
        Mockito.lenient().doAnswer(invocation -> {
            roomServiceInjected.deleteRoom(invocation.getArgument(0));
            return null;
        }).when(roomService).deleteRoom(any(Long.class));

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
        Allure.suite("Room API Controller Tests");
        initMocks();
    }

    @Step("Create room")
    void ValidateCreateRoom(Map<String, Object> request_body, HttpStatusCode expectedStatusCode) {
        RoomProvider.CreateRoom(lComponent.executor, lComponent.loginData, request_body, expectedStatusCode);;
    }

    @Step("Get rooms")
    void ValidateGetRooms(HttpStatusCode expectedStatusCode) {
        RoomProvider.GetRooms(lComponent.executor, lComponent.loginData, expectedStatusCode);
    }

    @Step("Get room by id")
    void ValidateGetRoomById(RoomRequestUrlParameters params, HttpStatusCode expectedStatusCode) {
        RoomProvider.GetRoomById(lComponent.executor, lComponent.loginData, params, expectedStatusCode);
    }

    @Step("Update room")
    void ValidateUpdateRoomById(Map<String, Object> request_body, RoomRequestUrlParameters params, HttpStatusCode expectedStatusCode) {
        RoomProvider.UpdateRoomById(lComponent.executor, lComponent.loginData, request_body, params, expectedStatusCode);
    }

    @Step("Delete room")
    void ValidateDeleteRoomById(RoomRequestUrlParameters params, HttpStatusCode expectedStatusCode) {
        RoomProvider.DeleteRoomById(lComponent.executor, lComponent.loginData, params, expectedStatusCode);
    }

    public static Stream<Arguments> NonPrivilegedRoleTypes() {
        RoleEntity.Type[] nonPrivilegedTypes = {RoleEntity.Type.USER, RoleEntity.Type.STUDENT, RoleEntity.Type.LECTURER,
                                                RoleEntity.Type.ORGANIZER, RoleEntity.Type.SYSADMIN};
        return Arrays.stream(nonPrivilegedTypes).map(Arguments::of);
    }

    public static Stream<Arguments> RoomRequestFields() {
        String[] field_names = {"location", "capacity", "description"};
        return Arrays.stream(field_names).map(Arguments::of);
    }

    public static Stream<Arguments> MissingRoomRequestFields() {
        String[] field_names = {"location", "capacity"};
        return Arrays.stream(field_names).map(Arguments::of);
    }

    public static Stream<Arguments> EmptyRoomRequestFields() {
        String[] field_names = {"location", "description"};
        return Arrays.stream(field_names).map(Arguments::of);
    }

    @AfterEach
    void resetMocks() {
        UserRepositoryMockProvider.resetMocks();
        RoleRepositoryMockProvider.resetMocks();
        RoomRepositoryMockProvider.resetMocks();
    }

    @Nested
    public class RoomsTests {
        @Nested
        public class CreateRoomTests {
            void SetAllureTestSubSuite() {
                Allure.label("subSuite", "POST /api/v1/rooms");
            }

            @Test
            @DisplayName("When request is valid, POST /api/v1/rooms returns 201 and valid response")
            @Description("When request is valid, POST /api/v1/rooms returns 201 and valid response.")
            void CreateRoomSuccessWithAllowedRole() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                ValidateCreateRoom(room.toMap(), HttpStatus.CREATED);
            }

            @Test
            @DisplayName("When valid request body without description field, POST /api/v1/rooms returns 201 and valid response")
            @Description("When valid request body without description field, POST /api/v1/rooms returns 201 and valid response.")
            void CreateRoomSuccessOnRoomWithoutDescription() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                var request_body = room.toMap();
                request_body.remove("description");
                ValidateCreateRoom(request_body, HttpStatus.CREATED);
            }

            @Test
            @DisplayName("When request without body, POST /api/v1/rooms returns 400")
            @Description("When request without body, POST /api/v1/rooms returns 400.")
            void CreateRoomWithEmptyRequestBody() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                ValidateCreateRoom(Map.of(), HttpStatus.BAD_REQUEST);
            }

            @Test
            @DisplayName("When valid request body with unnecessary field, POST /api/v1/rooms returns 400")
            @Description("When valid request body with unnecessary field, POST /api/v1/rooms returns 400.")
            void CreateRoomWithAdditionalRequestField() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                var request_map = room.toMap();
                request_map.put("VgbrsnbrsB", "sfhHFgsDfas");
                ValidateCreateRoom(request_map, HttpStatus.BAD_REQUEST);
            }

            @ParameterizedTest
            @MethodSource("education.kub.backend.ce.domain.room.controller.RoomControllerTests#MissingRoomRequestFields")
            @DisplayName("When request body without required field, POST /api/v1/rooms returns 400")
            @Description("When request body without required field, POST /api/v1/rooms returns 400.")
            void CreateRoomWithoutRequestField(String request_field) {
                SetAllureTestSubSuite();
                Allure.parameter("Request field", request_field);

                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();

                var request_body = room.toMap();
                request_body.remove(request_field);

                ValidateCreateRoom(request_body, HttpStatus.BAD_REQUEST);
            }

            @ParameterizedTest
            @MethodSource("education.kub.backend.ce.domain.room.controller.RoomControllerTests#EmptyRoomRequestFields")
            @DisplayName("When request body with empty field, POST /api/v1/rooms returns 422")
            @Description("When request body with empty field, POST /api/v1/rooms returns 422.")
            void CreateRoomWithEmptyRequestField(String request_field) {
                SetAllureTestSubSuite();
                Allure.parameter("Request field", request_field);

                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();

                var request_body = room.toMap();
                request_body.put(request_field, "");

                ValidateCreateRoom(request_body, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            @Test
            @DisplayName("When request body with invalid capacity field, PUT /api/v1/rooms/{id} returns 400")
            @Description("When request body with invalid capacity field, PUT /api/v1/rooms/{id} returns 400.")
            void CreateRoomWithInvalidRequestBodyCapacityField() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                var request_body = room.toMap();
                request_body.put("capacity", "sfhHFgsDfas");
                ValidateCreateRoom(request_body, HttpStatus.BAD_REQUEST);
            }


            @ParameterizedTest
            @MethodSource("education.kub.backend.ce.domain.room.controller.RoomControllerTests#NonPrivilegedRoleTypes")
            @DisplayName("When valid request with missing roles, POST /api/v1/rooms returns 401")
            @Description("When valid request with missing roles, POST /api/v1/rooms returns 401.")
            void CreateRoomWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                SetAllureTestSubSuite();
                Allure.parameter("Role of creator", user_creator_role);
                uComponent.addRole(user_creator_role);
                lComponent.FirstLogin();
                ValidateCreateRoom(room.toMap(), HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When request body with location field length what exceeds max possible length (256), POST /api/v1/rooms returns 422")
            @Description("When request body with location field length what exceeds max possible length (256), POST /api/v1/rooms returns 422.")
            void CreateRoomWithTooLongLocationFieldString() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                var request_body = room.toMap();
                request_body.put("location", "1".repeat(257));
                ValidateCreateRoom(request_body, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            @Test
            @DisplayName("When request body with negative capacity field value, POST /api/v1/rooms returns 422")
            @Description("When request body with negative capacity field value, POST /api/v1/rooms returns 422.")
            void CreateRoomWithNegativeCapacityFieldValue() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                var request_body = room.toMap();
                request_body.put("capacity", -1);
                ValidateCreateRoom(request_body, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            @Test
            @DisplayName("When request body with zero capacity field value, POST /api/v1/rooms returns 422")
            @Description("When request body with zero capacity field value, POST /api/v1/rooms returns 422.")
            void CreateRoomWithZeroCapacityFieldValue() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                var request_body = room.toMap();
                request_body.put("capacity", 0);
                ValidateCreateRoom(request_body, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            @Test
            @DisplayName("When request body with capacity field value what exceeds max possible value (32767), POST /api/v1/rooms returns 400")
            @Description("When request body with capacity field value what exceeds max possible value (32767), POST /api/v1/rooms returns 400.")
            void CreateRoomWithTooBigCapacityFieldValue() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                var request_body = room.toMap();
                request_body.put("capacity", 32768);
                ValidateCreateRoom(room.toMap(), HttpStatus.BAD_REQUEST);
            }

            @Test
            @DisplayName("When request without access token header, POST /api/v1/rooms returns 401")
            @Description("When request without access token header, POST /api/v1/rooms returns 401.")
            void CreateUserWithoutToken() {
                SetAllureTestSubSuite();
                ValidateCreateRoom(room.toMap(), HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When request with invalid access token header, POST /api/v1/rooms returns 401")
            @Description("When request with invalid access token header, POST /api/v1/rooms returns 401.")
            void CreateUserWithInvalidToken() {
                SetAllureTestSubSuite();
                lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                ValidateCreateRoom(room.toMap(), HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When valid request with location field equal to another existing room, POST /api/v1/rooms returns 409")
            @Description("When valid request with location field equal to another existing room, POST /api/v1/rooms returns 409.")
            void CreateUserWithSameLocation() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                var request_body = room.toMap();
                ValidateCreateRoom(request_body, HttpStatus.CREATED);
                ValidateCreateRoom(request_body, HttpStatus.CONFLICT);
            }
        }

        @Nested
        public class GetRoomsTests {
            void SetAllureTestSubSuite() {
                Allure.label("subSuite", "GET /api/v1/rooms");
            }

            @Test
            @DisplayName("When request is valid, GET /api/v1/rooms returns 200 and valid response")
            @Description("When request is valid, GET /api/v1/rooms returns 200 and valid response.")
            void GetRoomsSuccessWithAllowedRole() {
                SetAllureTestSubSuite();
                uComponent.addRole(RoleEntity.Type.ADMIN);
                lComponent.FirstLogin();
                ValidateCreateRoom(room.toMap(), HttpStatus.CREATED);
                ValidateGetRooms(HttpStatus.OK);
            }

            @ParameterizedTest
            @MethodSource("education.kub.backend.ce.domain.room.controller.RoomControllerTests#NonPrivilegedRoleTypes")
            @DisplayName("When valid request with missing roles, GET /api/v1/rooms returns 401")
            @Description("When valid request with missing roles, GET /api/v1/rooms returns 401.")
            void GetRoomsWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                SetAllureTestSubSuite();
                Allure.parameter("Role of creator", user_creator_role);
                uComponent.addRole(user_creator_role);
                lComponent.FirstLogin();
                ValidateGetRooms(HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When request without access token header, GET /api/v1/rooms returns 401")
            @Description("When request without access token header, GET /api/v1/rooms returns 401.")
            void GetRoomsWithoutToken() {
                SetAllureTestSubSuite();
                ValidateGetRooms(HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When request with invalid access token header, GET /api/v1/rooms returns 401")
            @Description("When request with invalid access token header, GET /api/v1/rooms returns 401.")
            void GetRoomsWithInvalidToken() {
                SetAllureTestSubSuite();
                lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                ValidateGetRooms(HttpStatus.UNAUTHORIZED);
            }
        }

        @Nested
        public class RoomTests {
            @Nested
            public class GetRoomTests {
                void SetAllureTestSubSuite() {
                    Allure.label("subSuite", "GET /api/v1/rooms/{id}");
                }

                @Test
                @DisplayName("When request is valid, GET /api/v1/rooms/{id} returns 200 and valid response")
                @Description("When request is valid, GET /api/v1/rooms/{id} returns 200 and valid response.")
                void GetRoomSuccessWithAllowedRole() {
                    SetAllureTestSubSuite();
                    lComponent.FirstLogin();
                    ValidateCreateRoom(room.toMap(), HttpStatus.CREATED);
                    ValidateGetRoomById(room.toUrlParameters(), HttpStatus.OK);
                }

                @Test
                @DisplayName("When request with invalid id url parameter, GET /api/v1/rooms/{id} returns 400")
                @Description("When request with invalid id url parameter, GET /api/v1/rooms/{id} returns 400.")
                void GetRoomWithInvalidIdUrlParameter() {
                    SetAllureTestSubSuite();
                    lComponent.FirstLogin();
                    ValidateGetRoomById(RoomRequestUrlParameters.builder().room_id("afsfdsfdsfss").build(), HttpStatus.BAD_REQUEST);
                }

//                @ParameterizedTest
//                @MethodSource("education.kub.backend.ce.domain.room.controller.RoomControllerTests#NonPrivilegedRoleTypes")
//                @DisplayName("When valid request with missing roles, GET /api/v1/rooms/{id} returns 401")
//                @Description("When valid request with missing roles, GET /api/v1/rooms/{id} returns 401.")
//                void GetRoomWithNotAllowedRole(RoleEntity.Type user_creator_role) {
//                    SetAllureTestSubSuite();
//                    Allure.parameter("Role of creator", user_creator_role);
//                    uComponent.addRole(user_creator_role);
//                    lComponent.FirstLogin();
//                    ValidateGetRoomById(room.toUrlParameters(), HttpStatus.UNAUTHORIZED);
//                }

                @Test
                @DisplayName("When request without access token header, GET /api/v1/rooms/{id} returns 401")
                @Description("When request without access token header, GET /api/v1/rooms/{id} returns 401.")
                void GetRoomWithoutToken() {
                    SetAllureTestSubSuite();
                    ValidateGetRoomById(room.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with invalid access token header, GET /api/v1/rooms/{id} returns 401")
                @Description("When request with invalid access token header, GET /api/v1/rooms/{id} returns 401.")
                void GetRoomWithInvalidToken() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                    ValidateGetRoomById(room.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with room id url parameter for not existing room, GET /api/v1/rooms/{id} returns 404")
                @Description("When request with room id url parameter for not existing room, GET /api/v1/rooms/{id} returns 404.")
                void GetRoomWithNonExistedRoom() {
                    SetAllureTestSubSuite();
                    lComponent.FirstLogin();
                    ValidateGetRoomById(RoomRequestUrlParameters.builder().room_id("123124314").build(), HttpStatus.NOT_FOUND);
                }
            }

            @Nested
            public class UpdateRoomTests {
                void SetAllureTestSubSuite() {
                    Allure.label("subSuite", "PUT /api/v1/rooms/{id}");
                }

                @Test
                @DisplayName("When request is valid, PUT /api/v1/rooms/{id} returns 201 and valid response")
                @Description("When request is valid, PUT /api/v1/rooms/{id} returns 201 and valid response.")
                void UpdateRoomSuccessWithAllowedRole() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.OK);
                }

                @Test
                @DisplayName("When valid request body without description field, PUT /api/v1/rooms/{id} returns 201 and valid response")
                @Description("When valid request body without description field, PUT /api/v1/rooms/{id} returns 201 and valid response.")
                void UpdateRoomSuccessOnRoomWithoutDescription() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    lComponent.FirstLogin();
                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.remove("description");
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.OK);
                }

                @Test
                @DisplayName("When request with invalid room id url parameter, PUT /api/v1/rooms/{id} returns 400")
                @Description("When request with invalid room id url parameter, PUT /api/v1/rooms/{id} returns 400.")
                void UpdateRoomWithInvalidIdUrlParameter() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateUpdateRoomById(room.toMap(), RoomRequestUrlParameters.builder().room_id("afsfdsfdsfss").build(), HttpStatus.BAD_REQUEST);
                }

                @Test
                @DisplayName("When request without body, PUT /api/v1/rooms/{id} returns 400")
                @Description("When request without body, PUT /api/v1/rooms/{id} returns 400.")
                void UpdateRoomWithEmptyRequestBody() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateCreateRoom(room.toMap(), HttpStatus.CREATED);
                    ValidateUpdateRoomById(Map.of(), room.toUrlParameters(), HttpStatus.BAD_REQUEST);
                }

                @Test
                @DisplayName("When valid request body with unnecessary field, PUT /api/v1/rooms/{id} returns 400")
                @Description("When valid request body with unnecessary field, PUT /api/v1/rooms/{id} returns 400.")
                void UpdateRoomWithAdditionalRequestField() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.put("VgbrsnbrsB", "sfhHFgsDfas");
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.BAD_REQUEST);
                }

                @Test
                @DisplayName("When request body with invalid capacity field, PUT /api/v1/rooms/{id} returns 400")
                @Description("When request body with invalid capacity field, PUT /api/v1/rooms/{id} returns 400.")
                void UpdateRoomWithInvalidRequestBodyCapacityField() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.put("capacity", "sfhHFgsDfas");
                    ValidateUpdateRoomById(Map.of(), room.toUrlParameters(), HttpStatus.BAD_REQUEST);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.room.controller.RoomControllerTests#RoomRequestFields")
                @DisplayName("When request body without optional field, PUT /api/v1/rooms/{id} returns 200")
                @Description("When request body without optional field, PUT /api/v1/rooms/{id} returns 200.")
                void UpdateRoomSuccessWithoutOptionalRequestField(String request_field) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Request field", request_field);

                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();

                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.remove(request_field);
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.OK);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.room.controller.RoomControllerTests#EmptyRoomRequestFields")
                @DisplayName("When request body with empty field, PUT /api/v1/rooms/{id} returns 422")
                @Description("When request body with empty field, PUT /api/v1/rooms/{id} returns 422.")
                void UpdateRoomWithEmptyLocationField(String request_field) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Request field", request_field);

                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();

                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.put(request_field, "");
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.room.controller.RoomControllerTests#NonPrivilegedRoleTypes")
                @DisplayName("When valid request with missing roles, PUT /api/v1/rooms/{id} returns 401")
                @Description("When valid request with missing roles, PUT /api/v1/rooms/{id} returns 401.")
                void UpdateRoomWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Role of creator", user_creator_role);
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    var request_body = room.toMap();
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request body with location field length what exceeds max possible length (256), PUT /api/v1/rooms/{id} returns 422")
                @Description("When request body with location field length what exceeds max possible length (256), PUT /api/v1/rooms/{id} returns 422.")
                void CreateRoomWithTooLongLocationFieldString() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.put("location", "1".repeat(257));
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @Test
                @DisplayName("When request body with negative capacity field value, PUT /api/v1/rooms/{id} returns 422")
                @Description("When request body with negative capacity field value, PUT /api/v1/rooms/{id} returns 422.")
                void CreateRoomWithNegativeCapacityFieldValue() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.put("capacity", -1);
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @Test
                @DisplayName("When request body with zero capacity field value, PUT /api/v1/rooms/{id} returns 422")
                @Description("When request body with zero capacity field value, PUT /api/v1/rooms/{id} returns 422.")
                void UpdateRoomWithZeroCapacityFieldValue() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.put("capacity", 0);
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @Test
                @DisplayName("When request body with capacity field value what exceeds max possible value (32767), PUT /api/v1/rooms/{id} returns 400")
                @Description("When request body with capacity field value what exceeds max possible value (32767), PUT /api/v1/rooms/{id} returns 400.")
                void UpdateRoomWithTooBigCapacityFieldValue() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.put("capacity", 32768);
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.BAD_REQUEST);
                }

                @Test
                @DisplayName("When request without access token header, PUT /api/v1/rooms/{id} returns 401")
                @Description("When request without access token header, PUT /api/v1/rooms/{id} returns 401.")
                void UpdateRoomWithoutToken() {
                    SetAllureTestSubSuite();
                    var request_body = room.toMap();
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with invalid access token header, PUT /api/v1/rooms/{id} returns 401")
                @Description("When request with invalid access token header, PUT /api/v1/rooms/{id} returns 401.")
                void UpdateRoomWithInvalidToken() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                    ValidateUpdateRoomById(room.toMap(), room.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When valid request with location field equal to another existing room, PUT /api/v1/rooms/{id} returns 409")
                @Description("When valid request with location field equal to another existing room, PUT /api/v1/rooms/{id} returns 409.")
                void UpdateRoomWithSameLocation() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var request_body = room.toMap();
                    request_body.put("location", "First location");
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.put("location", "Second location");
                    ValidateCreateRoom(request_body, HttpStatus.CREATED);
                    request_body.put("location", "First location");
                    ValidateUpdateRoomById(request_body, room.toUrlParameters(), HttpStatus.CONFLICT);
                }

                @Test
                @DisplayName("When request with room id url parameter for not existing room, PUT /api/v1/rooms/{id} returns 404")
                @Description("When request with room id url parameter for not existing room, PUT /api/v1/rooms/{id} returns 404.")
                void UpdateRoomWithNonExistedRoom() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateUpdateRoomById(room.toMap(), RoomRequestUrlParameters.builder().room_id("123124314").build(),
                            HttpStatus.NOT_FOUND);
                }
            }

            @Nested
            public class DeleteRoomTests {
                void SetAllureTestSubSuite() {
                    Allure.label("subSuite", "DELETE /api/v1/rooms/{id}");
                }

                @Test
                @DisplayName("When request is valid, DELETE /api/v1/rooms/{id} returns 200")
                @Description("When request is valid, DELETE /api/v1/rooms/{id} returns 200.")
                void DeleteRoomSuccessWithAllowedRole() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateCreateRoom(room.toMap(), HttpStatus.CREATED);
                    ValidateDeleteRoomById(room.toUrlParameters(), HttpStatus.NO_CONTENT);
                }

                @Test
                @DisplayName("When request with invalid room id url parameter, DELETE /api/v1/rooms/{id} returns 400")
                @Description("When request with invalid room id url parameter, DELETE /api/v1/rooms/{id} returns 400.")
                void DeleteRoomWithInvalidIdUrlParameter() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateDeleteRoomById(RoomRequestUrlParameters.builder().room_id("afsfdsfdsfss").build(), HttpStatus.BAD_REQUEST);
                }
                
                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.room.controller.RoomControllerTests#NonPrivilegedRoleTypes")
                @DisplayName("When valid request with missing roles, DELETE /api/v1/rooms/{id} returns 401")
                @Description("When valid request with missing roles, DELETE /api/v1/rooms/{id} returns 401.")
                void DeleteRoomWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Role of creator", user_creator_role);
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    ValidateDeleteRoomById(room.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request without access token header, DELETE /api/v1/rooms/{id} returns 401")
                @Description("When request without access token header, DELETE /api/v1/rooms/{id} returns 401.")
                void DeleteRoomWithoutToken() {
                    SetAllureTestSubSuite();
                    ValidateDeleteRoomById(room.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with invalid access token header, DELETE /api/v1/rooms/{id} returns 401")
                @Description("When request with invalid access token header, DELETE /api/v1/rooms/{id} returns 401.")
                void DeleteRoomWithInvalidToken() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                    ValidateDeleteRoomById(room.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with id url parameter for not existing room, DELETE /api/v1/rooms/{id} returns 404")
                @Description("When request with id url parameter for not existing room, DELETE /api/v1/rooms/{id} returns 404.")
                void DeleteRoomWithNonExistedRoom() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateDeleteRoomById(RoomRequestUrlParameters.builder().room_id("123124314").build(), HttpStatus.NOT_FOUND);
                }
            }
        }
    }
}
