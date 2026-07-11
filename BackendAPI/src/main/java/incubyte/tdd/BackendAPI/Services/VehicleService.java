package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Dto.Request.VehicleSearchRequest;
import incubyte.tdd.BackendAPI.Entity.Vehicle;

import java.math.BigDecimal;
import java.util.List;

public interface VehicleService {

    Vehicle addVehicle(Vehicle vehicle);
    List<Vehicle> getAllVehicles();
    List<Vehicle> search(VehicleSearchRequest request);
    Vehicle updateVehicle(
            Long id,
            Vehicle vehicle
    );

    void deleteVehicle(Long id);

    Vehicle restockVehicle(
            Long id,
            int quantity
    );

}