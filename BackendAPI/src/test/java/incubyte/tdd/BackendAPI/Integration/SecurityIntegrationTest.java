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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RegisterService registerService;

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
}