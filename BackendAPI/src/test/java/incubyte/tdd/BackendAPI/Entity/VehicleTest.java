package incubyte.tdd.BackendAPI.Entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VehicleTest {

    @Test
    @DisplayName("TC-030: Should create vehicle with all required fields")
    void shouldCreateVehicle() {

        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .make("Toyota")
                .model("Fortuner")
                .category("SUV")
                .price(BigDecimal.valueOf(4500000))
                .quantity(10)
                .build();

        assertAll(

                () -> assertEquals(1L, vehicle.getId()),

                () -> assertEquals("Toyota", vehicle.getMake()),

                () -> assertEquals("Fortuner", vehicle.getModel()),

                () -> assertEquals("SUV", vehicle.getCategory()),

                () -> assertEquals(
                        BigDecimal.valueOf(4500000),
                        vehicle.getPrice()
                ),

                () -> assertEquals(
                        10,
                        vehicle.getQuantity()
                )

        );

    }
}
