package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Exception.InvalidQuantityException;
import incubyte.tdd.BackendAPI.Exception.OutOfStockException;
import incubyte.tdd.BackendAPI.Exception.VehicleNotFoundException;
import incubyte.tdd.BackendAPI.Repository.VehicleRepository;
import incubyte.tdd.BackendAPI.Services.impl.InventoryServiceImpl;
import incubyte.tdd.BackendAPI.common.constants.ErrorMessages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private InventoryServiceImpl service;

    @Test
    @DisplayName("TC-040: Should increase vehicle quantity after restocking")
    void shouldIncreaseVehicleQuantityAfterRestock() {

        // Arrange
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .make("Toyota")
                .model("Fortuner")
                .category("SUV")
                .price(BigDecimal.valueOf(4500000))
                .quantity(10)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(repository.save(any(Vehicle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Vehicle updatedVehicle =
                service.restockVehicle(1L, 5);

        // Assert
        assertAll(
                () -> assertEquals(15, updatedVehicle.getQuantity()),
                () -> verify(repository).save(vehicle)
        );
    }

    @Test
    @DisplayName("TC-041: Should throw exception when restocking non-existing vehicle")
    void shouldThrowExceptionWhenRestockingUnknownVehicle() {

        // Arrange
        Long vehicleId = 100L;

        when(repository.findById(vehicleId))
                .thenReturn(Optional.empty());

        // Act
        VehicleNotFoundException exception = assertThrows(
                VehicleNotFoundException.class,
                () -> service.restockVehicle(vehicleId, 5)
        );

        // Assert
        assertEquals(
                ErrorMessages.VEHICLE_NOT_FOUND + vehicleId,
                exception.getMessage()
        );

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("TC-042: Should reject invalid restock quantity")
    void shouldRejectInvalidRestockQuantity() {

        // Arrange
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .make("Toyota")
                .model("Fortuner")
                .quantity(10)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        // Act
        InvalidQuantityException exception =
                assertThrows(
                        InvalidQuantityException.class,
                        () -> service.restockVehicle(1L, 0)
                );

        // Assert
        assertEquals(
                ErrorMessages.INVALID_QUANTITY,
                exception.getMessage()
        );

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("TC-043: Should decrease vehicle quantity after purchase")
    void shouldDecreaseVehicleQuantityAfterPurchase() {

        // Arrange
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .make("Toyota")
                .model("Fortuner")
                .quantity(10)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(repository.save(any(Vehicle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Vehicle updatedVehicle = service.purchaseVehicle(1L);

        // Assert
        assertAll(
                () -> assertEquals(9, updatedVehicle.getQuantity()),
                () -> verify(repository).save(vehicle)
        );
    }

    @Test
    @DisplayName("TC-044: Should reject purchase when stock is zero")
    void shouldRejectPurchaseWhenStockIsZero() {

        // Arrange
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .make("Toyota")
                .model("Fortuner")
                .quantity(0)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        // Act
        OutOfStockException exception = assertThrows(
                OutOfStockException.class,
                () -> service.purchaseVehicle(1L)
        );

        // Assert
        assertEquals(
                ErrorMessages.OUT_OF_STOCK,
                exception.getMessage()
        );

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("TC-045: Should throw exception when purchasing non-existing vehicle")
    void shouldThrowExceptionWhenPurchasingUnknownVehicle() {

        // Arrange
        Long vehicleId = 100L;

        when(repository.findById(vehicleId))
                .thenReturn(Optional.empty());

        // Act
        VehicleNotFoundException exception = assertThrows(
                VehicleNotFoundException.class,
                () -> service.purchaseVehicle(vehicleId)
        );

        // Assert
        assertEquals(
                ErrorMessages.VEHICLE_NOT_FOUND + vehicleId,
                exception.getMessage()
        );

        verify(repository, never()).save(any());
    }

}
