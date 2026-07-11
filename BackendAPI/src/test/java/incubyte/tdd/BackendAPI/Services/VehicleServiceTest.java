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

}