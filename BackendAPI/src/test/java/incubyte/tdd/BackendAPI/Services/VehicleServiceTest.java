package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Exception.DuplicateVehicleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import incubyte.tdd.BackendAPI.Repository.VehicleRepository;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Services.impl.VehicleServiceImpl;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private VehicleServiceImpl service;

    @Test
    @DisplayName("TC-031: Should add a new vehicle successfully")
    void shouldAddVehicleSuccessfully() {

        Vehicle vehicle = Vehicle.builder()
                .make("Toyota")
                .model("Fortuner")
                .category("SUV")
                .price(BigDecimal.valueOf(4500000))
                .quantity(10)
                .build();

        when(repository.save(any(Vehicle.class)))
                .thenReturn(vehicle);

        Vehicle savedVehicle = service.addVehicle(vehicle);

        assertAll(

                () -> assertNotNull(savedVehicle),

                () -> assertEquals(
                        "Toyota",
                        savedVehicle.getMake()),

                () -> assertEquals(
                        "Fortuner",
                        savedVehicle.getModel()),

                () -> verify(repository)
                        .save(vehicle)

        );

    }


    @Test
    @DisplayName("TC-032: Should reject duplicate vehicle")
    void shouldRejectDuplicateVehicle() {

        // Arrange
        Vehicle vehicle = Vehicle.builder()
                .make("Toyota")
                .model("Fortuner")
                .category("SUV")
                .price(BigDecimal.valueOf(4500000))
                .quantity(10)
                .build();

        when(repository.existsByMakeAndModel(
                vehicle.getMake(),
                vehicle.getModel()
        )).thenReturn(true);

        // Act
        DuplicateVehicleException exception =
                assertThrows(
                        DuplicateVehicleException.class,
                        () -> service.addVehicle(vehicle)
                );

        // Assert
        assertEquals(
                "Vehicle already exists.",
                exception.getMessage()
        );

        verify(repository, never()).save(any());
    }


    @Test
    @DisplayName("TC-033: Should return all vehicles")
    void shouldReturnAllVehicles() {

        // Arrange
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
                        .quantity(8)
                        .build()

        );

        when(repository.findAll())
                .thenReturn(vehicles);

        // Act
        List<Vehicle> result = service.getAllVehicles();

        // Assert
        assertAll(

                () -> assertEquals(2, result.size()),

                () -> assertEquals(
                        "Toyota",
                        result.get(0).getMake()
                ),

                () -> assertEquals(
                        "Honda",
                        result.get(1).getMake()
                )

        );

        verify(repository).findAll();
    }

    @Test
    @DisplayName("TC-034: Should search vehicles by make")
    void shouldSearchVehicleByMake() {

        // Arrange
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
                        .make("Toyota")
                        .model("Innova")
                        .category("MPV")
                        .price(BigDecimal.valueOf(3000000))
                        .quantity(5)
                        .build()

        );

        when(repository.findByMakeIgnoreCase("Toyota"))
                .thenReturn(vehicles);

        // Act
        List<Vehicle> result =
                service.searchByMake("Toyota");

        // Assert
        assertAll(

                () -> assertEquals(2, result.size()),

                () -> assertEquals(
                        "Toyota",
                        result.get(0).getMake()
                ),

                () -> assertEquals(
                        "Toyota",
                        result.get(1).getMake()
                )

        );

        verify(repository)
                .findByMakeIgnoreCase("Toyota");
    }

    @Test
    @DisplayName("TC-035: Should search vehicles by model")
    void shouldSearchVehicleByModel() {

        // Arrange
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
                        .make("Toyota")
                        .model("Fortuner")
                        .category("SUV")
                        .price(BigDecimal.valueOf(4700000))
                        .quantity(8)
                        .build()

        );

        when(repository.findByModelIgnoreCase("Fortuner"))
                .thenReturn(vehicles);

        // Act
        List<Vehicle> result = service.searchByModel("Fortuner");

        // Assert
        assertAll(

                () -> assertEquals(2, result.size()),

                () -> assertEquals(
                        "Fortuner",
                        result.get(0).getModel()
                ),

                () -> assertEquals(
                        "Fortuner",
                        result.get(1).getModel()
                )

        );

        verify(repository)
                .findByModelIgnoreCase("Fortuner");
    }

}