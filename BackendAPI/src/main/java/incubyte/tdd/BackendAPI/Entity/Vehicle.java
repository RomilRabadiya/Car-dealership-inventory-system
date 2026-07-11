package incubyte.tdd.BackendAPI.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;

    private String model;

    private String category;

    private BigDecimal price;

    private Integer quantity;

    public void updateDetails(Vehicle updatedVehicle) {
        this.make = updatedVehicle.getMake();
        this.model = updatedVehicle.getModel();
        this.category = updatedVehicle.getCategory();
        this.price = updatedVehicle.getPrice();
        this.quantity = updatedVehicle.getQuantity();
    }

    public void purchase() {
        if (this.quantity <= 0) {
            throw new incubyte.tdd.BackendAPI.Exception.OutOfStockException(
                    "Vehicle is out of stock."
            );
        }
        this.quantity--;
    }

    public void restock(int quantity) {
        validateQuantity(quantity);
        this.quantity += quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new incubyte.tdd.BackendAPI.Exception.InvalidQuantityException(
                    "Quantity must be greater than zero."
            );
        }
    }

}