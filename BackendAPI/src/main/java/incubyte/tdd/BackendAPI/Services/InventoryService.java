package incubyte.tdd.BackendAPI.Services;

import incubyte.tdd.BackendAPI.Entity.Vehicle;

public interface InventoryService {

    Vehicle purchaseVehicle(Long id);

    Vehicle restockVehicle(Long id, int quantity);

}
