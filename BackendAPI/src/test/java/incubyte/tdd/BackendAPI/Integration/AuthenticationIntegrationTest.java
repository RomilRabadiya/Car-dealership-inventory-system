package incubyte.tdd.BackendAPI.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import incubyte.tdd.BackendAPI.Dto.Request.LoginRequest;
import incubyte.tdd.BackendAPI.Dto.Request.RegisterRequest;
import incubyte.tdd.BackendAPI.Dto.Response.LoginResponse;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("TC-055: Should complete authentication flow successfully")
    void shouldCompleteAuthenticationFlow() throws Exception {
        // Act: Register
        registerUser();

        // Act: Login
        String token = loginAndGetToken();

        // Assert: Verify Token & Database
        assertNotNull(token);

        User user = repository.findByEmail("romil@gmail.com").orElseThrow();

        assertEquals("Romil", user.getName());
        assertNotEquals("password123", user.getPassword());
    }

    private void registerUser() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("Romil", "romil@gmail.com", "password123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());
    }

    private String loginAndGetToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("romil@gmail.com", "password123");
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();

        LoginResponse loginResponse = mapper.readValue(response, LoginResponse.class);
        return loginResponse.getToken();
    }
}