package incubyte.tdd.BackendAPI.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import incubyte.tdd.BackendAPI.Services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import incubyte.tdd.BackendAPI.Entity.Vehicle;
import java.math.BigDecimal;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private VehicleService service;

    @Test
    @DisplayName("TC-48: Should create vehicle")
    void shouldCreateVehicle() throws Exception {

        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .make("Toyota")
                .model("Fortuner")
                .category("SUV")
                .price(BigDecimal.valueOf(4500000))
                .quantity(10)
                .build();

        when(service.addVehicle(any(Vehicle.class)))
                .thenReturn(vehicle);

        mockMvc.perform(
                        post("/api/vehicles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(vehicle))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.make").value("Toyota"));

        verify(service).addVehicle(any(Vehicle.class));
    }

    @Test
    @DisplayName("TC-49: Should update vehicle")
    void shouldUpdateVehicle() throws Exception {

        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .make("Toyota")
                .model("Fortuner Legender")
                .category("SUV")
                .price(BigDecimal.valueOf(5000000))
                .quantity(15)
                .build();

        when(service.updateVehicle(eq(1L), any(Vehicle.class)))
                .thenReturn(vehicle);

        mockMvc.perform(
                        put("/api/vehicles/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(vehicle))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model")
                        .value("Fortuner Legender"));

        verify(service).updateVehicle(eq(1L), any(Vehicle.class));
    }

    @Test
    @DisplayName("TC-50: Should delete vehicle")
    void shouldDeleteVehicle() throws Exception {

        doNothing().when(service).deleteVehicle(1L);

        mockMvc.perform(
                        delete("/api/vehicles/1")
                )
                .andExpect(status().isNoContent());

        verify(service).deleteVehicle(1L);
    }

}
