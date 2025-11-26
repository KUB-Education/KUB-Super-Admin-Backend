package education.kub.backend.ce.domain.educational_program.controller;

import education.kub.backend.ce.app.filter.JwtAuthFilter;
import education.kub.backend.ce.app.properties.AppAccountRegistrationProperties;
import education.kub.backend.ce.domain.auth.controller.AuthController;
import education.kub.backend.ce.domain.auth.model.LoginRequest;
import education.kub.backend.ce.domain.auth.service.AuthService;
import education.kub.backend.ce.domain.educational_program.model.EducationalProgramUpdateRequest;
import education.kub.backend.ce.domain.educational_program.service.EducationalProgramService;
import education.kub.backend.ce.domain.role.entity.RoleEntity;
import education.kub.backend.ce.domain.user.mapper.UserMapper;
import education.kub.backend.ce.infrastructure.components.auth.LoginComponent;
import education.kub.backend.ce.infrastructure.components.study_field.StudyFieldComponent;
import education.kub.backend.ce.infrastructure.components.user.UserComponent;
import education.kub.backend.ce.infrastructure.properties.auth.LoginProperties;
import education.kub.backend.ce.infrastructure.properties.executor.ConnectionProperties;
import education.kub.backend.ce.infrastructure.properties.study_field.*;
import education.kub.backend.ce.infrastructure.properties.user.UserProperties;
import education.kub.backend.ce.infrastructure.providers.allure.SuiteHierarchy;
import education.kub.backend.ce.infrastructure.providers.mocks.repositories.*;
import education.kub.backend.ce.infrastructure.providers.request_wrappers.domain.EducationalProgramProvider;
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
public class EducationalProgramControllerTests {
    private final LoginProperties loginData = new LoginProperties();
    private final LoginComponent lComponent = new LoginComponent();

    private final UserProperties user_creator = UserProperties.builder().build();

    public StudyFieldProperties study_field = StudyFieldProperties.builder().build();
    public SpecialtyProperties specialty = SpecialtyProperties.builder().build();
    public EducationalProgramProperties program = EducationalProgramProperties.builder().build();

    @Autowired
    private ConnectionProperties conn;
    @Autowired
    private UserComponent uComponent;
    @Autowired
    private StudyFieldComponent studyFieldComponent;
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
    private EducationalProgramController educationalProgramController;
    @MockitoBean
    private EducationalProgramService educationalProgramService;
    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    public void initMocks() {
        loginData.setUser(user_creator);
        lComponent.executor.setConn(conn);
        lComponent.setLoginData(loginData);
        uComponent.setUserData(loginData.user);
        uComponent.mockRepos();
        studyFieldComponent.mockRepos();

        studyFieldComponent.saveStudyField(study_field);
        studyFieldComponent.saveSpecialty(study_field, specialty);

        lComponent.executor.mvc = mockMvc;

        JwtAuthFilter jwtAuthFilterInjected = new JwtAuthFilter(jwtTokenProvider, uComponent.tokenStoreService);

        AuthService authServiceInjected = new AuthService(uComponent.userRepo, uComponent.passwordService,
                uComponent.tokenStoreService, jwtTokenProvider);

        EducationalProgramService educationalProgramServiceInjected = studyFieldComponent.BuildEducationalProgramService();

        Mockito.lenient().doAnswer(invocation -> {
            return authServiceInjected.login(invocation.getArgument(0));
        }).when(authService).login(any(LoginRequest.class));

        Mockito.lenient().doAnswer(invocation -> {
            return educationalProgramServiceInjected.getAllEducationalPrograms();
        }).when(educationalProgramService).getAllEducationalPrograms();
        Mockito.lenient().doAnswer(invocation -> {
            return educationalProgramServiceInjected.getEducationalProgramById(invocation.getArgument(0, Long.class));
        }).when(educationalProgramService).getEducationalProgramById(any(Long.class));
        Mockito.lenient().doAnswer(invocation -> {
            return educationalProgramServiceInjected.updateEducationalProgram(invocation.getArgument(0), invocation.getArgument(1));
        }).when(educationalProgramService).updateEducationalProgram(any(Long.class), any(EducationalProgramUpdateRequest.class));
        Mockito.lenient().doAnswer(invocation -> {
            educationalProgramServiceInjected.deleteEducationalProgram(invocation.getArgument(0, Long.class));
            return null;
        }).when(educationalProgramService).deleteEducationalProgram(any(Long.class));

        try {
            Mockito.lenient().doAnswer(invocation -> {
                jwtAuthFilterInjected.doFilter(invocation.getArgument(0),
                        invocation.getArgument(1), invocation.getArgument(2));
                return null;
            }).when(jwtAuthFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
        }
        catch (Exception _) {}
    }

    @BeforeEach
    public void Setup() {
        SuiteHierarchy.SetAllureTestHierarchy();
        Allure.suite("Educational Program API Controller Tests");
        initMocks();
    }

    @Step("Get educational programs")
    void ValidateGetPrograms(HttpStatusCode expectedStatusCode) {
        EducationalProgramProvider.GetEducationalPrograms(lComponent.executor, lComponent.loginData, expectedStatusCode);
    }

    @Step("Get educational program by id")
    void ValidateGetProgramById(EducationalProgramRequestUrlParameters params, HttpStatusCode expectedStatusCode) {
        EducationalProgramProvider.GetEducationalProgramById(lComponent.executor, lComponent.loginData, params, expectedStatusCode);
    }

    @Step("Update educational program")
    void ValidateUpdateProgramById(Map<String, Object> request_body, EducationalProgramRequestUrlParameters params, HttpStatusCode expectedStatusCode) {
        EducationalProgramProvider.UpdateEducationalProgramById(lComponent.executor, lComponent.loginData, request_body, params, expectedStatusCode);
    }

    @Step("Delete educational program")
    void ValidateDeleteProgramById(EducationalProgramRequestUrlParameters params, HttpStatusCode expectedStatusCode) {
        EducationalProgramProvider.DeleteEducationalProgramById(lComponent.executor, lComponent.loginData, params, expectedStatusCode);
    }

    public static Stream<Arguments> NonPrivilegedRoleTypes() {
        RoleEntity.Type[] nonPrivilegedTypes = {RoleEntity.Type.USER, RoleEntity.Type.STUDENT, RoleEntity.Type.LECTURER,
                RoleEntity.Type.ORGANIZER, RoleEntity.Type.SYSADMIN};
        return Arrays.stream(nonPrivilegedTypes).map(Arguments::of);
    }

    public static Stream<Arguments> EducationalProgramRequestFields() {
        String[] field_names = {"specialty_id", "name", "degree_type", "study_form", "duration"};
        return Arrays.stream(field_names).map(Arguments::of);
    }

    public static Stream<Arguments> EmptyEducationalProgramRequestFields() {
        String[] field_names = {"name", "degree_type", "study_form"};
        return Arrays.stream(field_names).map(Arguments::of);
    }

    public static Stream<Arguments> NumericEducationalProgramRequestFields() {
        String[] field_names = {"specialty_id", "duration"};
        return Arrays.stream(field_names).map(Arguments::of);
    }

    @AfterEach
    void resetMocks() {
        UserRepositoryMockProvider.resetMocks();
        RoleRepositoryMockProvider.resetMocks();
        StudyFieldRepositoryMockProvider.resetMocks();
        SpecialtyRepositoryMockProvider.resetMocks();
        EducationalProgramRepositoryMockProvider.resetMocks();
    }

    @Nested
    public class EducationalProgramsTests {
        @Nested
        public class GetEducationalProgramsTests {
            void SetAllureTestSubSuite() {
                Allure.label("subSuite", "GET /api/v1/educational-programs");
            }

            @Test
            @DisplayName("When request is valid, GET /api/v1/educational-programs returns 200 and valid response")
            @Description("When request is valid, GET /api/v1/educational-programs returns 200 and valid response.")
            void GetProgramsSuccess() {
                SetAllureTestSubSuite();
                lComponent.FirstLogin();
                studyFieldComponent.saveEducationalProgram(specialty, program);
                ValidateGetPrograms(HttpStatus.OK);
            }

            @Test
            @DisplayName("When request without access token header, GET /api/v1/educational-programs returns 401")
            @Description("When request without access token header, GET /api/v1/educational-programs returns 401.")
            void GetProgramsWithoutToken() {
                SetAllureTestSubSuite();
                ValidateGetPrograms(HttpStatus.UNAUTHORIZED);
            }

            @Test
            @DisplayName("When request with invalid access token header, GET /api/v1/educational-programs returns 401")
            @Description("When request with invalid access token header, GET /api/v1/educational-programs returns 401.")
            void GetProgramsWithInvalidToken() {
                SetAllureTestSubSuite();
                lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                ValidateGetPrograms(HttpStatus.UNAUTHORIZED);
            }
        }

        @Nested
        public class EducationalProgramTests {
            @Nested
            public class GetEducationalProgramTests {
                void SetAllureTestSubSuite() {
                    Allure.label("subSuite", "GET /api/v1/educational-programs/{id}");
                }

                @Test
                @DisplayName("When request is valid, GET /api/v1/educational-programs/{id} returns 200 and valid response")
                @Description("When request is valid, GET /api/v1/educational-programs/{id} returns 200 and valid response.")
                void GetProgramSuccess() {
                    SetAllureTestSubSuite();
                    lComponent.FirstLogin();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    ValidateGetProgramById(program.toUrlParameters(), HttpStatus.OK);
                }

                @Test
                @DisplayName("When request with invalid id url parameter, GET /api/v1/educational-programs/{id} returns 400")
                @Description("When request with invalid id url parameter, GET /api/v1/educational-programs/{id} returns 400.")
                void GetProgramWithInvalidIdUrlParameter() {
                    SetAllureTestSubSuite();
                    lComponent.FirstLogin();
                    ValidateGetProgramById(EducationalProgramRequestUrlParameters.builder().program_id("afsfdsfdsfss").build(),
                            HttpStatus.BAD_REQUEST);
                }

                @Test
                @DisplayName("When request without access token header, GET /api/v1/educational-programs/{id} returns 401")
                @Description("When request without access token header, GET /api/v1/educational-programs/{id} returns 401.")
                void GetProgramWithoutToken() {
                    SetAllureTestSubSuite();
                    ValidateGetProgramById(program.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with invalid access token header, GET /api/v1/educational-programs/{id} returns 401")
                @Description("When request with invalid access token header, GET /api/v1/educational-programs/{id} returns 401.")
                void GetProgramWithInvalidToken() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                    ValidateGetProgramById(program.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with Program id url parameter for not existing Program, GET /api/v1/educational-programs/{id} returns 404")
                @Description("When request with Program id url parameter for not existing Program, GET /api/v1/educational-programs/{id} returns 404.")
                void GetProgramWithNonExistedProgram() {
                    SetAllureTestSubSuite();
                    lComponent.FirstLogin();
                    ValidateGetProgramById(EducationalProgramRequestUrlParameters.builder().program_id("123124314").build(),
                            HttpStatus.NOT_FOUND);
                }
            }

            @Nested
            public class UpdateEducationalProgramTests {
                void SetAllureTestSubSuite() {
                    Allure.label("subSuite", "PUT /api/v1/programs/{id}");
                }

                @Test
                @DisplayName("When request is valid, PUT /api/v1/programs/{id} returns 201 and valid response")
                @Description("When request is valid, PUT /api/v1/programs/{id} returns 201 and valid response.")
                void UpdateProgramSuccessWithAllowedRole() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    ValidateUpdateProgramById(program.toMap(), program.toUrlParameters(), HttpStatus.OK);
                }

                @Test
                @DisplayName("When request with invalid program id url parameter, PUT /api/v1/programs/{id} returns 400")
                @Description("When request with invalid program id url parameter, PUT /api/v1/programs/{id} returns 400.")
                void UpdateProgramWithInvalidIdUrlParameter() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateUpdateProgramById(program.toMap(), EducationalProgramRequestUrlParameters.builder().program_id("afsfdsfdsfss").build(), HttpStatus.BAD_REQUEST);
                }

                @Test
                @DisplayName("When request without body, PUT /api/v1/programs/{id} returns 400")
                @Description("When request without body, PUT /api/v1/programs/{id} returns 400.")
                void UpdateProgramWithEmptyRequestBody() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    ValidateUpdateProgramById(Map.of(), program.toUrlParameters(), HttpStatus.BAD_REQUEST);
                }

                @Test
                @DisplayName("When valid request body with unnecessary field, PUT /api/v1/programs/{id} returns 400")
                @Description("When valid request body with unnecessary field, PUT /api/v1/programs/{id} returns 400.")
                void UpdateProgramWithAdditionalRequestField() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    var request_body = program.toMap();
                    request_body.put("VgbrsnbrsB", "sfhHFgsDfas");
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.BAD_REQUEST);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.educational_program.controller.EducationalProgramControllerTests#NumericEducationalProgramRequestFields")
                @DisplayName("When request body with invalid specialty_id field, PUT /api/v1/programs/{id} returns 400")
                @Description("When request body with invalid specialty_id field, PUT /api/v1/programs/{id} returns 400.")
                void UpdateProgramWithInvalidRequestBodyNumericField(String request_field) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Request field", request_field);

                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();

                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    var request_body = program.toMap();
                    request_body.put(request_field, "sfhHFgsDfas");
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.BAD_REQUEST);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.educational_program.controller.EducationalProgramControllerTests#EducationalProgramRequestFields")
                @DisplayName("When request body without optional field, PUT /api/v1/programs/{id} returns 200")
                @Description("When request body without optional field, PUT /api/v1/programs/{id} returns 200.")
                void UpdateProgramSuccessWithoutOptionalRequestField(String request_field) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Request field", request_field);

                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();

                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    var request_body = program.toMap();
                    request_body.remove(request_field);
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.OK);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.educational_program.controller.EducationalProgramControllerTests#EmptyEducationalProgramRequestFields")
                @DisplayName("When request body with empty field, PUT /api/v1/programs/{id} returns 422")
                @Description("When request body with empty field, PUT /api/v1/programs/{id} returns 422.")
                void UpdateProgramWithEmptyRequestField(String request_field) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Request field", request_field);

                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();

                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    var request_body = program.toMap();
                    request_body.put(request_field, "");
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.educational_program.controller.EducationalProgramControllerTests#NonPrivilegedRoleTypes")
                @DisplayName("When valid request with missing roles, PUT /api/v1/programs/{id} returns 401")
                @Description("When valid request with missing roles, PUT /api/v1/programs/{id} returns 401.")
                void UpdateProgramWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Role of creator", user_creator_role);
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    var request_body = program.toMap();
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request body with name field length what exceeds max possible length (128), PUT /api/v1/programs/{id} returns 422")
                @Description("When request body with name field length what exceeds max possible length (128), PUT /api/v1/programs/{id} returns 422.")
                void CreateProgramWithTooLongNameFieldString() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    var request_body = program.toMap();
                    request_body.put("name", "1".repeat(129));
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @Test
                @DisplayName("When request body with invalid degree_type enum field, PUT /api/v1/programs/{id} returns 422")
                @Description("When request body with invalid degree_type enum field, PUT /api/v1/programs/{id} returns 422.")
                void CreateProgramWithInvalidDegreeTypeFieldString() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    var request_body = program.toMap();
                    request_body.put("degree_type", "agasfadasda");
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @Test
                @DisplayName("When request body with invalid study_form enum field, PUT /api/v1/programs/{id} returns 422")
                @Description("When request body with invalid study_form enum field, PUT /api/v1/programs/{id} returns 422.")
                void CreateProgramWithInvalidStudyFormFieldString() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    var request_body = program.toMap();
                    request_body.put("study_form", "agasfadasda");
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @Test
                @DisplayName("When request body with negative duration field value, PUT /api/v1/programs/{id} returns 422")
                @Description("When request body with negative duration field value, PUT /api/v1/programs/{id} returns 422.")
                void CreateProgramWithNegativeDurationFieldValue() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    var request_body = program.toMap();
                    request_body.put("duration", -1);
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @Test
                @DisplayName("When request body with negative specialty_id field value, PUT /api/v1/programs/{id} returns 422")
                @Description("When request body with negative specialty_id field value, PUT /api/v1/programs/{id} returns 422.")
                void CreateProgramWithNegativeSpecialtyIdFieldValue() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    var request_body = program.toMap();
                    request_body.put("specialty_id", -1);
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

                @Test
                @DisplayName("When request body with zero duration field value, PUT /api/v1/programs/{id} returns 422")
                @Description("When request body with zero duration field value, PUT /api/v1/programs/{id} returns 422.")
                void UpdateProgramWithZeroDurationFieldValue() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var request_body = program.toMap();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    request_body.put("duration", 0);
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
                }

//                @Test
//                @DisplayName("When request body with specialty_id field value what exceeds max possible value (9223372036854775807), PUT /api/v1/programs/{id} returns 422")
//                @Description("When request body with specialty_id field value what exceeds max possible value (9223372036854775807), PUT /api/v1/programs/{id} returns 422.")
//                void UpdateProgramWithTooBigSpecialtyIdFFieldValue() {
//                    SetAllureTestSubSuite();
//                    uComponent.addRole(RoleEntity.Type.ADMIN);
//                    lComponent.FirstLogin();
//                    studyFieldComponent.saveEducationalProgram(specialty, program);
//                    var request_body = program.toMap();
//                    request_body.remove("location");
//                    request_body.put("specialty_id", new BigInteger("9223372036854775808"));
//                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.UNPROCESSABLE_ENTITY);
//                }

                @Test
                @DisplayName("When request body with duration field value what exceeds max possible value (32767), PUT /api/v1/programs/{id} returns 400")
                @Description("When request body with duration field value what exceeds max possible value (32767), PUT /api/v1/programs/{id} returns 400.")
                void UpdateProgramWithTooBigDurationFieldValue() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    var request_body = program.toMap();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    request_body.remove("location");
                    request_body.put("duration", 32768);
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.BAD_REQUEST);
                }

                @Test
                @DisplayName("When request without access token header, PUT /api/v1/programs/{id} returns 401")
                @Description("When request without access token header, PUT /api/v1/programs/{id} returns 401.")
                void UpdateProgramWithoutToken() {
                    SetAllureTestSubSuite();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    var request_body = program.toMap();
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with invalid access token header, PUT /api/v1/programs/{id} returns 401")
                @Description("When request with invalid access token header, PUT /api/v1/programs/{id} returns 401.")
                void UpdateProgramWithInvalidToken() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                    ValidateUpdateProgramById(program.toMap(), program.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When valid request with location field equal to another existing Program, PUT /api/v1/programs/{id} returns 409")
                @Description("When valid request with location field equal to another existing Program, PUT /api/v1/programs/{id} returns 409.")
                void UpdateProgramWithSameLocation() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    program.setName("First program");
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    program.setName("Second second");
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    program.setName("First program");
                    var request_body = program.toMap();
                    ValidateUpdateProgramById(request_body, program.toUrlParameters(), HttpStatus.CONFLICT);
                }

                @Test
                @DisplayName("When request with program id url parameter for not existing Program, PUT /api/v1/programs/{id} returns 404")
                @Description("When request with program id url parameter for not existing Program, PUT /api/v1/programs/{id} returns 404.")
                void UpdateProgramWithNonExistedProgram() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateUpdateProgramById(program.toMap(), EducationalProgramRequestUrlParameters.builder().program_id("123124314").build(),
                            HttpStatus.NOT_FOUND);
                }
            }

            @Nested
            public class DeleteEducationalProgramTests {
                void SetAllureTestSubSuite() {
                    Allure.label("subSuite", "DELETE /api/v1/educational-programs/{id}");
                }

                @Test
                @DisplayName("When request is valid, DELETE /api/v1/educational-programs/{id} returns 200")
                @Description("When request is valid, DELETE /api/v1/educational-programs/{id} returns 200.")
                void DeleteProgramSuccessWithAllowedRole() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    studyFieldComponent.saveEducationalProgram(specialty, program);
                    ValidateDeleteProgramById(program.toUrlParameters(), HttpStatus.NO_CONTENT);
                }

                @Test
                @DisplayName("When request with invalid program id url parameter, DELETE /api/v1/educational-programs/{id} returns 400")
                @Description("When request with invalid program id url parameter, DELETE /api/v1/educational-programs/{id} returns 400.")
                void DeleteProgramWithInvalidIdUrlParameter() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateDeleteProgramById(EducationalProgramRequestUrlParameters.builder().program_id("afsfdsfdsfss").build(), HttpStatus.BAD_REQUEST);
                }

                @ParameterizedTest
                @MethodSource("education.kub.backend.ce.domain.educational_program.controller.EducationalProgramControllerTests#NonPrivilegedRoleTypes")
                @DisplayName("When valid request with missing roles, DELETE /api/v1/educational-programs/{id} returns 401")
                @Description("When valid request with missing roles, DELETE /api/v1/educational-programs/{id} returns 401.")
                void DeleteProgramWithNotAllowedRole(RoleEntity.Type user_creator_role) {
                    SetAllureTestSubSuite();
                    Allure.parameter("Role of creator", user_creator_role);
                    uComponent.addRole(user_creator_role);
                    lComponent.FirstLogin();
                    ValidateDeleteProgramById(program.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request without access token header, DELETE /api/v1/educational-programs/{id} returns 401")
                @Description("When request without access token header, DELETE /api/v1/educational-programs/{id} returns 401.")
                void DeleteProgramWithoutToken() {
                    SetAllureTestSubSuite();
                    ValidateDeleteProgramById(program.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with invalid access token header, DELETE /api/v1/educational-programs/{id} returns 401")
                @Description("When request with invalid access token header, DELETE /api/v1/educational-programs/{id} returns 401.")
                void DeleteProgramWithInvalidToken() {
                    SetAllureTestSubSuite();
                    lComponent.loginData.accessToken = "asfadfdgwWe2qe2e1easfar2r653e";
                    ValidateDeleteProgramById(program.toUrlParameters(), HttpStatus.UNAUTHORIZED);
                }

                @Test
                @DisplayName("When request with id url parameter for not existing program, DELETE /api/v1/educational-programs/{id} returns 404")
                @Description("When request with id url parameter for not existing program, DELETE /api/v1/educational-programs/{id} returns 404.")
                void DeleteProgramWithNonExistedProgram() {
                    SetAllureTestSubSuite();
                    uComponent.addRole(RoleEntity.Type.ADMIN);
                    lComponent.FirstLogin();
                    ValidateDeleteProgramById(EducationalProgramRequestUrlParameters.builder().program_id("123124314").build(), HttpStatus.NOT_FOUND);
                }
            }
        }
    }
}
