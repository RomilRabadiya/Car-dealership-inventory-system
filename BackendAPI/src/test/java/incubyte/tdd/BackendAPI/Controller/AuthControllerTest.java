package incubyte.tdd.BackendAPI.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import incubyte.tdd.BackendAPI.Dto.Request.RegisterRequest;
import incubyte.tdd.BackendAPI.Dto.Response.UserResponse;
import incubyte.tdd.BackendAPI.Services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Controller (MockMvc) : HTTP status codes, request validation, JSON request/response mapping

// Controller tests for AuthController using MockMvc
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    // Mock the service layer
    @MockBean
    UserService service;

    // Convert Java objects to JSON
    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("TC-007: Should return 201 when registration is successful")
    void shouldReturnCreatedWhenRegisterSuccessfully() throws Exception {

        // Arrange: Create a registration request
        RegisterRequest request = new RegisterRequest(
                "Romil",
                "romil@gmail.com",
                "password123"
        );

        // Mock the service response
        UserResponse response = new UserResponse(
                1L,
                "Romil",
                "romil@gmail.com"
        );

        when(service.register(any()))
                .thenReturn(response);

        // Act & Assert: Perform POST request and verify the response
        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Romil"))
                .andExpect(jsonPath("$.email").value("romil@gmail.com"));
    }


    @Test
    @DisplayName("TC-008: Should return 400 when request body is invalid")
    void shouldReturnBadRequestForInvalidRequest() throws Exception {

        // Arrange: Create an invalid registration request
        RegisterRequest request = new RegisterRequest(
                "",
                "invalid-email",
                "123"
        );

        // Act & Assert: Verify that the controller returns HTTP 400 for invalid input
        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TC-009: Should return validation error response")
    void shouldReturnValidationErrors() throws Exception {

        // Arrange: Create an invalid registration request
        RegisterRequest request = new RegisterRequest(
                "",
                "invalid-email",
                "123"
        );

        // Act & Assert: Perform the registration request and verify the validation error response
        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        mapper.writeValueAsString(request)
                                )
                )

                // Verify the HTTP status code
                .andExpect(status().isBadRequest())

                // Verify the general error response
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation Failed"))

                // Verify field-specific validation errors
                .andExpect(jsonPath("$.errors.name")
                        .value("Name is required."))
                .andExpect(jsonPath("$.errors.email")
                        .value("Invalid email format."))
                .andExpect(jsonPath("$.errors.password")
                        .value("Password must be at least 8 characters."));
    }
}