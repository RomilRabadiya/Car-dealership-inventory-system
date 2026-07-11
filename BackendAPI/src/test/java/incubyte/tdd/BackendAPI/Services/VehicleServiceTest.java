package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Exception.DuplicateVehicleException;
import incubyte.tdd.BackendAPI.Exception.InvalidQuantityException;
import incubyte.tdd.BackendAPI.Exception.OutOfStockException;
import incubyte.tdd.BackendAPI.Exception.VehicleNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import incubyte.tdd.BackendAPI.Repository.VehicleRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Services.impl.VehicleServiceImpl;
import incubyte.tdd.BackendAPI.Dto.Request.VehicleSearchRequest;

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
                vehicle.getModel())).thenReturn(true);

        // Act
        DuplicateVehicleException exception = assertThrows(
                DuplicateVehicleException.class,
                () -> service.addVehicle(vehicle));

        // Assert
        assertEquals(
                "Vehicle already exists.",
                exception.getMessage());

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
                        result.get(0).getMake()),

                () -> assertEquals(
                        "Honda",
                        result.get(1).getMake())

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

        VehicleSearchRequest request = VehicleSearchRequest.builder()
                .make("Toyota")
                .build();

        when(repository.findAll(any(org.springframework.data.jpa.domain.Specification.class)))
                .thenReturn(vehicles);

        // Act
        List<Vehicle> result = service.search(request);

        // Assert
        assertAll(

                () -> assertEquals(2, result.size()),

                () -> assertEquals(
                        "Toyota",
                        result.get(0).getMake()),

                () -> assertEquals(
                        "Toyota",
                        result.get(1).getMake())

        );

        verify(repository)
                .findAll(any(org.springframework.data.jpa.domain.Specification.class));
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

        VehicleSearchRequest request = VehicleSearchRequest.builder()
                .model("Fortuner")
                .build();

        when(repository.findAll(any(org.springframework.data.jpa.domain.Specification.class)))
                .thenReturn(vehicles);

        // Act
        List<Vehicle> result = service.search(request);

        // Assert
        assertAll(

                () -> assertEquals(2, result.size()),

                () -> assertEquals(
                        "Fortuner",
                        result.get(0).getModel()),

                () -> assertEquals(
                        "Fortuner",
                        result.get(1).getModel())

        );

        verify(repository)
                .findAll(any(org.springframework.data.jpa.domain.Specification.class));
    }

    @Test
    @DisplayName("TC-036: Should search vehicles by category")
    void shouldSearchVehicleByCategory() {

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
                        .make("Mahindra")
                        .model("Scorpio")
                        .category("SUV")
                        .price(BigDecimal.valueOf(2200000))
                        .quantity(5)
                        .build()

        );

        VehicleSearchRequest request = VehicleSearchRequest.builder()
                .category("SUV")
                .build();

        when(repository.findAll(any(org.springframework.data.jpa.domain.Specification.class)))
                .thenReturn(vehicles);

        // Act
        List<Vehicle> result = service.search(request);

        // Assert
        assertAll(

                () -> assertEquals(2, result.size()),

                () -> assertEquals(
                        "SUV",
                        result.get(0).getCategory()),

                () -> assertEquals(
                        "SUV",
                        result.get(1).getCategory())

        );

        verify(repository)
                .findAll(any(org.springframework.data.jpa.domain.Specification.class));
    }

    @Test
    @DisplayName("TC-037: Should search vehicles by price range")
    void shouldSearchVehicleByPriceRange() {

        // Arrange
        BigDecimal minPrice = BigDecimal.valueOf(1000000);
        BigDecimal maxPrice = BigDecimal.valueOf(3000000);

        List<Vehicle> vehicles = List.of(

                Vehicle.builder()
                        .id(1L)
                        .make("Honda")
                        .model("City")
                        .category("Sedan")
                        .price(BigDecimal.valueOf(1800000))
                        .quantity(8)
                        .build(),

                Vehicle.builder()
                        .id(2L)
                        .make("Mahindra")
                        .model("Scorpio")
                        .category("SUV")
                        .price(BigDecimal.valueOf(2200000))
                        .quantity(5)
                        .build()

        );

        VehicleSearchRequest request = VehicleSearchRequest.builder()
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();

        when(repository.findAll(any(org.springframework.data.jpa.domain.Specification.class)))
                .thenReturn(vehicles);

        // Act
        List<Vehicle> result = service.search(request);

        // Assert
        assertAll(

                () -> assertEquals(2, result.size()),

                () -> assertTrue(
                        result.get(0).getPrice()
                                .compareTo(minPrice) >= 0),

                () -> assertTrue(
                        result.get(1).getPrice()
                                .compareTo(maxPrice) <= 0)

        );

        verify(repository)
                .findAll(any(org.springframework.data.jpa.domain.Specification.class));
    }

    @Test
    @DisplayName("TC-038: Should update vehicle successfully")
    void shouldUpdateVehicleSuccessfully() {

        // Arrange
        Vehicle existingVehicle = Vehicle.builder()
                .id(1L)
                .make("Toyota")
                .model("Fortuner")
                .category("SUV")
                .price(BigDecimal.valueOf(4500000))
                .quantity(10)
                .build();

        Vehicle updatedVehicle = Vehicle.builder()
                .make("Toyota")
                .model("Fortuner Legender")
                .category("SUV")
                .price(BigDecimal.valueOf(5000000))
                .quantity(15)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingVehicle));

        when(repository.save(any(Vehicle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Vehicle result = service.updateVehicle(1L, updatedVehicle);

        // Assert
        assertAll(

                () -> assertEquals(
                        "Fortuner Legender",
                        result.getModel()),

                () -> assertEquals(
                        BigDecimal.valueOf(5000000),
                        result.getPrice()),

                () -> assertEquals(
                        15,
                        result.getQuantity())

        );

        verify(repository).save(existingVehicle);
    }

    @Test
    @DisplayName("TC-039: Should delete vehicle successfully")
    void shouldDeleteVehicleSuccessfully() {

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

        // Act
        service.deleteVehicle(1L);

        // Assert
        verify(repository).delete(vehicle);
    }


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

                () -> assertEquals(
                        15,
                        updatedVehicle.getQuantity()
                ),

                () -> verify(repository)
                        .save(vehicle)

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
                "Vehicle not found with id: 100",
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
                "Quantity must be greater than zero.",
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

                () -> assertEquals(
                        9,
                        updatedVehicle.getQuantity()
                ),

                () -> verify(repository)
                        .save(vehicle)

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
                "Vehicle is out of stock.",
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
                "Vehicle not found with id: 100",
                exception.getMessage()
        );

        verify(repository, never()).save(any());
    }

}