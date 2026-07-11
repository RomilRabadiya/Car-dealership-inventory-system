package incubyte.tdd.BackendAPI.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import incubyte.tdd.BackendAPI.Dto.Request.LoginRequest;
import incubyte.tdd.BackendAPI.Dto.Response.LoginResponse;
import incubyte.tdd.BackendAPI.Entity.Role;
import incubyte.tdd.BackendAPI.Entity.User;
import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class SearchFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    @DisplayName("TC-058: Should complete vehicle search workflow")
    void shouldCompleteVehicleSearchWorkflow() throws Exception {

        // Arrange
        registerAdmin();

        String token = loginAsAdmin();

        createVehicle(
                token,
                "Toyota",
                "Fortuner",
                "SUV",
                BigDecimal.valueOf(4500000),
                10);

        createVehicle(
                token,
                "Honda",
                "City",
                "Sedan",
                BigDecimal.valueOf(1800000),
                5);

        // Search by Make
        mockMvc.perform(

                get("/api/vehicles/search")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("make", "Toyota")

        )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].make")
                        .value("Toyota"));

        // Search by Category
        mockMvc.perform(

                get("/api/vehicles/search")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("category", "Sedan")

        )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].category")
                        .value("Sedan"));

    }

    private void registerAdmin() {
        User admin = User.builder()
                .name("Admin")
                .email("admin@gmail.com")
                .password(encoder.encode("admin123"))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
    }

    private String loginAsAdmin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin@gmail.com", "admin123");
        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();

        LoginResponse loginResponse = mapper.readValue(response, LoginResponse.class);
        return loginResponse.getToken();
    }

    private void createVehicle(
            String token,
            String make,
            String model,
            String category,
            BigDecimal price,
            int quantity) throws Exception {
        Vehicle vehicle = Vehicle.builder()
                .make(make)
                .model(model)
                .category(category)
                .price(price)
                .quantity(quantity)
                .build();

        mockMvc.perform(post("/api/vehicles")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vehicle)))
                .andExpect(status().isCreated());
    }
}
