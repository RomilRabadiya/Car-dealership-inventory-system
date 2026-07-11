package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Entity.Vehicle;

import java.util.List;

public interface VehicleService {

    Vehicle addVehicle(Vehicle vehicle);
    List<Vehicle> getAllVehicles();
    List<Vehicle> findByMakeIgnoreCase(String make);

}