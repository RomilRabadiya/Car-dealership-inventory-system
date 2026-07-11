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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class VehicleCRUDFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    @DisplayName("TC-056: Should complete vehicle CRUD flow")
    void shouldCompleteVehicleCrudFlow() throws Exception {

        // Arrange
        registerAdmin();

        String token = loginAsAdmin();

        Vehicle vehicle = Vehicle.builder()
                .make("Toyota")
                .model("Fortuner")
                .category("SUV")
                .price(BigDecimal.valueOf(4500000))
                .quantity(10)
                .build();

        // Step 1 : Create Vehicle

        MvcResult createResult = mockMvc.perform(

                        post("/api/vehicles")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(vehicle))

                )

                .andExpect(status().isCreated())

                .andReturn();

        Vehicle createdVehicle = mapper.readValue(
                createResult.getResponse().getContentAsString(),
                Vehicle.class
        );

        // Step 2 : Get All Vehicles

        mockMvc.perform(

                        get("/api/vehicles")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        // Step 3 : Update Vehicle

        createdVehicle.setModel("Fortuner Legender");

        mockMvc.perform(

                        put("/api/vehicles/{id}", createdVehicle.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(createdVehicle))

                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model")
                        .value("Fortuner Legender"));

        // Step 4 : Delete Vehicle

        mockMvc.perform(

                        delete("/api/vehicles/{id}", createdVehicle.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                )

                .andExpect(status().isNoContent());

        // Step 5 : Verify Deletion

        mockMvc.perform(

                        get("/api/vehicles")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

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
}
