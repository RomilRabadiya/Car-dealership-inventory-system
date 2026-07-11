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

 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 @SpringBootTest
 @AutoConfigureMockMvc
 @Transactional
 @ActiveProfiles("test")
 public class InventoryFlowTest {

     @Autowired
     private MockMvc mockMvc;

     @Autowired
     private ObjectMapper mapper;

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private PasswordEncoder encoder;

     @Test
     @DisplayName("TC-057: Should complete inventory workflow")
     void shouldCompleteInventoryWorkflow() throws Exception {

         // Arrange
         registerAdmin();

         String token = loginAsAdmin();

         Vehicle vehicle = createVehicle(token);

         // Step 1 : Purchase Vehicle

         mockMvc.perform(

                         post("/api/vehicles/{id}/purchase", vehicle.getId())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)

                 )

                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.quantity").value(9));

         // Step 2 : Restock Vehicle

         mockMvc.perform(

                         post("/api/vehicles/{id}/restock", vehicle.getId())
                                 .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                 .param("quantity", "5")

                 )

                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.quantity").value(14));

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

     private Vehicle createVehicle(String token) throws Exception {
         Vehicle vehicle = Vehicle.builder()
                 .make("Toyota")
                 .model("Fortuner")
                 .category("SUV")
                 .price(BigDecimal.valueOf(4500000))
                 .quantity(10)
                 .build();

         MvcResult createResult = mockMvc.perform(
                 post("/api/vehicles")
                         .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(mapper.writeValueAsString(vehicle))
         ).andReturn();

         return mapper.readValue(createResult.getResponse().getContentAsString(), Vehicle.class);
     }
 }
