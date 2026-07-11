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
import incubyte.tdd.BackendAPI.Dto.Request.VehicleSearchRequest;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import incubyte.tdd.BackendAPI.Security.JwtService;
import incubyte.tdd.BackendAPI.Services.impl.CustomUserDetailsService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(VehicleController.class)
@AutoConfigureMockMvc(addFilters = false)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private VehicleService service;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

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

    @Test
    @DisplayName("TC-51: Should return all vehicles")
    void shouldReturnAllVehicles() throws Exception {

        List<Vehicle> vehicles = List.of(

                Vehicle.builder()
                        .id(1L)
                        .make("Toyota")
                        .model("Fortuner")
                        .category("SUV")
                        .price(BigDecimal.valueOf(4500000))
                        .quantity(10)
                        .build(),

                Vehicle.builder()
                        .id(2L)
                        .make("Honda")
                        .model("City")
                        .category("Sedan")
                        .price(BigDecimal.valueOf(1800000))
                        .quantity(5)
                        .build()

        );

        when(service.getAllVehicles())
                .thenReturn(vehicles);

        mockMvc.perform(

                        get("/api/vehicles")

                )

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].make").value("Toyota"))

                .andExpect(jsonPath("$[1].make").value("Honda"));

        verify(service).getAllVehicles();

    }

    @Test
    @DisplayName("TC-52: Should search vehicles by make")
    void shouldSearchVehiclesByMake() throws Exception {

        List<Vehicle> vehicles = List.of(

                Vehicle.builder()
                        .id(1L)
                        .make("Toyota")
                        .model("Fortuner")
                        .category("SUV")
                        .price(BigDecimal.valueOf(4500000))
                        .quantity(10)
                        .build()

        );

        when(service.search(any(VehicleSearchRequest.class)))
                .thenReturn(vehicles);

        mockMvc.perform(

                        get("/api/vehicles/search")

                                .param("make", "Toyota")

                )

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()").value(1))

                .andExpect(jsonPath("$[0].make").value("Toyota"));

        verify(service).search(any(VehicleSearchRequest.class));

    }

    @Test
    @DisplayName("TC-53: Should purchase vehicle")
    void shouldPurchaseVehicle() throws Exception {

        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .make("Toyota")
                .model("Fortuner")
                .category("SUV")
                .price(BigDecimal.valueOf(4500000))
                .quantity(9)
                .build();

        when(service.purchaseVehicle(1L))
                .thenReturn(vehicle);

        mockMvc.perform(

                        post("/api/vehicles/1/purchase")

                )

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(1))

                .andExpect(jsonPath("$.quantity").value(9));

        verify(service).purchaseVehicle(1L);

    }


    @Test
    @DisplayName("TC-54: Should restock vehicle")
    void shouldRestockVehicle() throws Exception {

        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .make("Toyota")
                .model("Fortuner")
                .category("SUV")
                .price(BigDecimal.valueOf(4500000))
                .quantity(15)
                .build();

        when(service.restockVehicle(1L, 5))
                .thenReturn(vehicle);

        mockMvc.perform(

                        post("/api/vehicles/1/restock")

                                .param("quantity", "5")

                )

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(1))

                .andExpect(jsonPath("$.quantity").value(15));

        verify(service).restockVehicle(1L, 5);

    }

}
