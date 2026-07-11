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

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

}