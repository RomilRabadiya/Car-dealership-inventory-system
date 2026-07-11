package incubyte.tdd.BackendAPI.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import incubyte.tdd.BackendAPI.Dto.Request.RegisterRequest;
import incubyte.tdd.BackendAPI.Dto.Response.UserResponse;
import incubyte.tdd.BackendAPI.Services.RegisterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import incubyte.tdd.BackendAPI.Security.JwtService;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

//    POST /api/vehicles
//    Authorization: Bearer eyJhbGciOiJIUzI1Ni...
//            ↓
//    JwtAuthenticationFilter
//            ↓
//    JwtService.validateToken()
//            ↓
//    CustomUserDetailsService
//            ↓
//    SecurityContextHolder
//            ↓
//    Controller

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RegisterService registerService;

    @MockBean
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    // POST /api/auth/register
    //
    //   ↓
    //
    //  No JWT
    @Test
    @DisplayName("TC-021: Register endpoint should be publicly accessible")
    void shouldAllowRegisterWithoutAuthentication() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "Romil",
                "romil@gmail.com",
                "password123"
        );

        UserResponse response = new UserResponse(
                1L,
                "Romil",
                "romil@gmail.com"
        );

        when(registerService.register(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());
    }

    //  Client
    //    ↓
    //  GET /api/vehicles
    //    ↓
    //  No Authorization Header
    //    ↓
    //  Spring Security
    //    ↓
    //  401 Unauthorized


    // After Test :
    //    FAILED
    //
    //    Expected : 401
    //
    //    Actual : 403


    //Why might you get 403?
    //If you're using Spring Security without configuring the authentication entry point, unauthenticated requests may result in 403 Forbidden instead of 401 Unauthorized.
    //For a REST API, 401 is the correct response for missing authentication.

    @Test
    @DisplayName("TC-022: Should reject protected endpoint without JWT")
    void shouldRejectProtectedEndpointWithoutAuthentication() throws Exception {

        mockMvc.perform(
                        get("/api/vehicles")
                )
                .andExpect(status().isUnauthorized());

    }

//    Requirement :
//    A valid JWT should authenticate the request and allow access to protected endpoints.

//    Request
//        ↓
//    Authorization Header
//    Bearer eyJhbGciOiJIUzI1Ni...
//        ↓
//    JwtAuthenticationFilter
//        ↓
//    Extract Token
//        ↓
//    Validate Token
//        ↓
//    Load User
//        ↓
//    SecurityContextHolder
//        ↓
//    Controller
    @Test
    @DisplayName("TC-023: Should allow authenticated user to access protected endpoint")
    void shouldAllowAccessWithValidAuthentication() throws Exception {

        mockMvc.perform(
                        get("/api/vehicles")
                                .with(user("romil@gmail.com")
                                        .roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Vehicle List"));
    }

//    Requirement :
//    A request containing a valid JWT should be authenticated through the Security Filter Chain.

//    HTTP Request
//        ↓
//    SecurityFilterChain
//        ↓
//    JwtAuthenticationFilter
//        ↓
//    JwtService
//        ↓
//    CustomUserDetailsService
//        ↓
//    SecurityContextHolder
//        ↓
//    VehicleController

    @Test
    @DisplayName("TC-025: Should allow access with valid JWT")
    void shouldAllowAccessWithValidJwt() throws Exception {

        // Arrange
        User user = User.builder()
                .id(1L)
                .name("Romil")
                .email("romil@gmail.com")
                .password("encryptedPassword")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(user);

        when(userRepository.findByEmail("romil@gmail.com"))
                .thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(
                        get("/api/vehicles")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Vehicle List"));
    }
}