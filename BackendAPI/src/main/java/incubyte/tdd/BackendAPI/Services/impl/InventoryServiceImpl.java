package incubyte.tdd.BackendAPI.Services.impl;

import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Exception.VehicleNotFoundException;
import incubyte.tdd.BackendAPI.Repository.VehicleRepository;
import incubyte.tdd.BackendAPI.Services.InventoryService;
import incubyte.tdd.BackendAPI.common.constants.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final VehicleRepository repository;

    private Vehicle getVehicleById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(
                        ErrorMessages.VEHICLE_NOT_FOUND + id
                ));
    }

    @Override
    public Vehicle restockVehicle(Long id, int quantity) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.restock(quantity);
        return repository.save(vehicle);
    }

    @Override
    public Vehicle purchaseVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.purchase();
        return repository.save(vehicle);
    }

}
