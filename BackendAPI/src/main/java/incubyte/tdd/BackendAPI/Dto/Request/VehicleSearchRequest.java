package incubyte.tdd.BackendAPI.Dto.Request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSearchRequest {

    private String make;

    private String model;

    private String category;

    @jakarta.validation.constraints.PositiveOrZero(message = "Minimum price must be positive or zero")
    private BigDecimal minPrice;

    @jakarta.validation.constraints.Positive(message = "Maximum price must be positive")
    private BigDecimal maxPrice;

}